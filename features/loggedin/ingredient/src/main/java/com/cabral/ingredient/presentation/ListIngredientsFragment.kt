package com.cabral.ingredient.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.cabral.arch.BaseFragment
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.design.R
import com.cabral.ingredient.databinding.IngredientsListFragmentBinding
import com.cabral.ingredient.presentation.adapter.ListIngredientAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.ingredient.R as IngredientR

class ListIngredientsFragment :
    BaseFragment<IngredientsListFragmentBinding>(IngredientsListFragmentBinding::inflate) {

    private lateinit var adapter: ListIngredientAdapter

    private val navigationIngredient: ListIngredientNavigation by inject()

    private val viewModel: ListIngredientsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initClicks()
        navigationIngredient.hasItemAddOnIngredient(this, viewLifecycleOwner) {
            viewModel.getAllIngredients()
        }
    }


    private fun initClicks() {
        binding.addIngredient.setOnClickListener {
            navigationIngredient.openIngredient(this)
        }
    }

    private fun initObservers() {
        viewModel.run {

            notifyStartLoading.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 0
            }

            notifyEmptyList.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 1
            }

            notifyListIngredient.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 2
                initAdapter()
            }

            notifySuccessRemove.observe(viewLifecycleOwner) {
                it.deleteItem()
            }

            notifyError.observe(viewLifecycleOwner) {

            }


        }
    }

    private fun initAdapter() {
        adapter = ListIngredientAdapter(requireContext()).apply {
            onClick = {
                Toast.makeText(requireContext(), it.name, Toast.LENGTH_LONG).show()
            }
            onClickTrash = {
                it.name?.let { it1 ->
                    showAlertDialog(
                        IngredientR.string.ingredients_remove_modal_title,
                        IngredientR.string.ingredients_warning_remove,
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