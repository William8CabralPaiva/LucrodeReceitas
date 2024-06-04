package com.cabral.recipe.presentation

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.cabral.arch.BaseFragment
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.arch.widget.CustomToast
import com.cabral.core.ListRecipeNavigation
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.design.R
import com.cabral.model.toRecipeArgs
import com.cabral.recipe.adapter.RecipeAdapter
import com.cabral.recipe.databinding.RecipeListFragmentBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListRecipeFragment :
    BaseFragment<RecipeListFragmentBinding>(RecipeListFragmentBinding::inflate) {

    private lateinit var adapter: RecipeAdapter

    private val navigationRecipe: ListRecipeNavigation by inject()

    private val viewModel: ListRecipeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        initObservers()

        navigationRecipe.observeRecipeChangeOnListRecipeFragment(this, viewLifecycleOwner) { it ->
            viewModel.getAllRecipe()
        }
    }

    private fun initObservers() {
        viewModel.run {

            notifyStartLoading.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.run {
                    binding.viewFlipper.displayedChild = 0
                }
            }

            notifyEmptyList.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.run {
                    binding.viewFlipper.displayedChild = 1
                }
            }

            notifyListRecipe.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.viewFlipper.displayedChild = 2
                    initAdapter()
                } else {
                    binding.viewFlipper.displayedChild = 1
                }
            }

            notifySuccessDelete.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.run {
                    deleteItem()
                    if (listRecipe.isEmpty()) {
                        binding.viewFlipper.displayedChild = 1
                    }
                }
            }

            notifyErrorDelete.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    val text = String.format(getString(R.string.design_delete_error), it)
                    showToast(text)
                }
            }

        }
    }

    private fun RecipeProfitPrice.deleteItem() {
        val position = viewModel.listRecipe.indexOf(this)
        if (position >= 0) {
            adapter.notifyItemRemoved(position)
            viewModel.listRecipe.remove(viewModel.listRecipe[position])
        }
    }

    private fun initAdapter() {
        adapter = RecipeAdapter(requireContext()).apply {
            onClick = {
                navigationRecipe.openRecipe(this@ListRecipeFragment, it.toRecipeArgs())
            }
            onClickEdit = {
                navigationRecipe.openCostsFragment(this@ListRecipeFragment, it.toRecipeArgs())
            }
            onClickTrash = {
                it.name?.let { name ->
                    showAlertDialog(
                        R.string.design_delete_title,
                        R.string.design_delete_message,
                        name
                    ) { viewModel.deleteRecipe(it) }
                }


            }
        }

        binding.recycleView.adapter = adapter
        adapter.submitList(viewModel.listRecipe)
    }

    private fun initClicks() {
        binding.addRecipe.setOnClickListener {
            navigationRecipe.openRecipe(this, null)
        }
    }


    private fun showToast(text: String) {
        CustomToast.Builder(requireContext())
            .message(text)
            .build().show()
    }

    private fun showAlertDialog(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        item: String,
        positiveFunction: () -> Unit
    ) {
        context?.let {
            val title = String.format(getString(titleRes), item)
            val message = String.format(getString(messageRes), item)
            CustomAlertDialog.Builder(requireContext())
                .title(title)
                .message(message)
                .negativeButton(getString(R.string.design_no))
                .positiveButton(getString(R.string.design_yes))
                .positiveFunction {
                    positiveFunction()
                }

                .build().show()
        }
    }
}