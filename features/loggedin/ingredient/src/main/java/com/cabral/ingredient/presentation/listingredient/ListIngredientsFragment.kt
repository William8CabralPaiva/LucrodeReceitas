package com.cabral.ingredient.presentation.listingredient

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.cabral.arch.BaseFragment
import com.cabral.arch.extensions.collectIn
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.arch.widget.CustomToast
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.design.R
import com.cabral.ingredient.databinding.IngredientsListFragmentBinding
import com.cabral.ingredient.presentation.adapter.ListIngredientAdapter
import com.cabral.model.toIngredientArgs
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class ListIngredientsFragment :
    BaseFragment<IngredientsListFragmentBinding>(IngredientsListFragmentBinding::inflate) {

    private lateinit var adapter: ListIngredientAdapter

    private val navigationIngredient: ListIngredientNavigation by inject()

    private val viewModel: ListIngredientsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initClicks()
        viewModel.getAllIngredients()
    }

    private fun initClicks() {
        binding.addIngredient.setOnClickListener {
            navigationIngredient.openIngredient(this)
        }

    }

    private fun initObservers() {

        viewModel.uiState.collectIn(this) {
            when (it) {
                is UiState.StartLoading -> {
                    binding.viewFlipper.displayedChild = 0
                }

                is UiState.EmptyList -> {
                    binding.viewFlipper.displayedChild = 1
                }

                is UiState.ListIngredient -> {
                    if (it.list.isNotEmpty()) {
                        binding.viewFlipper.displayedChild = 2
                        initAdapter()
                    } else {
                        binding.viewFlipper.displayedChild = 1
                    }
                }
            }
        }

        viewModel.uiEvent.collectIn(this) {
            when (it) {
                is UiEvent.SuccessRemoveIngredient -> {
                    it.ingredient.deleteItem()
                }

                is UiEvent.ErrorRemoveIngredient -> {
                    val text =
                        String.format(getString(R.string.design_delete_error), it.ingredientName)
                    showToast(text)
                }
            }
        }

    }

    private fun showToast(text: String) {
        context?.run {
            CustomToast.Builder(this)
                .setBackgroundColor(R.color.design_dark_red)
                .message(text)
                .build().show()
        }
    }

    private fun initAdapter() {
        adapter = ListIngredientAdapter(requireContext()).apply {
            onClick = {
                navigationIngredient.openIngredient(
                    this@ListIngredientsFragment,
                    it.toIngredientArgs()
                )
            }
            onClickTrash = {
                it.name?.let { it1 ->
                    showAlertDialog(
                        DesignR.string.design_remove_modal_title,
                        DesignR.string.design_warning_remove,
                        it1
                    ) {
                        viewModel.deleteIngredient(it)
                    }
                }
            }
        }

        binding.recycleView.adapter = adapter
        adapter.submitList(viewModel.listIngredient)
    }

    private fun Ingredient.deleteItem() {
        val position = viewModel.listIngredient.indexOf(this)
        if (position >= 0) {
            adapter.notifyItemRemoved(position)
            viewModel.listIngredient.remove(viewModel.listIngredient[position])
        }
        if (viewModel.listIngredient.isEmpty()) {
            binding.viewFlipper.displayedChild = 1
        }
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