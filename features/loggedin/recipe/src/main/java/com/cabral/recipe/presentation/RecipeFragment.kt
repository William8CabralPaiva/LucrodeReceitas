package com.cabral.recipe.presentation

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.cabral.arch.BaseFragment
import com.cabral.arch.widget.CustomToast
import com.cabral.core.ListRecipeNavigation
import com.cabral.core.common.domain.model.Recipe
import com.cabral.model.RecipeArgs
import com.cabral.model.toRecipe
import com.cabral.model.toRecipeArgs
import com.cabral.recipe.R
import com.cabral.recipe.databinding.RecipeFragmentBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeFragment : BaseFragment<RecipeFragmentBinding>(RecipeFragmentBinding::inflate) {

    private val navigation: ListRecipeNavigation by inject()

    private val args: RecipeFragmentArgs by navArgs()

    private val viewModel: RecipeViewModel by viewModel()

    private var alreadySetCurrentRecipe = false

    private fun initObservers() {
        viewModel.run {
            notifySuccess.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    showToast(R.string.recipe_success_save)
                    binding.abAdd.setAlpha(false)
                }

            }

            notifyError.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    showToast(R.string.recipe_save_error)
                }
            }

            notifyStopLoadingButton.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    binding.abSave.stopLoading()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.abAdd.setAlpha(true)
        initObservers()
        navigation.observeListRecipeHasChanged(this@RecipeFragment)

        args.currentRecipe?.let {
            if (!alreadySetCurrentRecipe) {
                viewModel.recipe = it.toRecipe()
                fillInputs(viewModel.recipe)
                alreadySetCurrentRecipe = true
                viewModel.recipeAlreadyCreate = true
            }
            binding.abAdd.setAlpha(false)
        }


        navigation.observeRecipeChangeOnRecipeAddEditFragment(this, viewLifecycleOwner) { recipe ->
            backFromAddRecipeEdit(recipe)
        }

        binding.abSave.abSetOnClickListener {
            binding.abSave.run {
                startLoading()
                viewModel.run {
                    if (validateFields()) {
                        saveRecipe()
                    } else {
                        stopLoading()
                    }
                }
            }
        }

        binding.abAdd.abSetOnClickListener {
            if (viewModel.recipeAlreadyCreate) {
                if (viewModel.recipe.ingredientList == null) {
                    viewModel.recipe.ingredientList = mutableListOf()
                }
                navigation.openAddEditIngredient(this, viewModel.recipe.toRecipeArgs())

            } else {
                showToast(R.string.recipe_save_before_add)
            }

        }

    }

    private fun backFromAddRecipeEdit(recipe: RecipeArgs?) {
        val ingredientList = recipe?.toRecipe()?.ingredientList
        if (viewModel.recipe.ingredientList != ingredientList) {
            viewModel.recipe.ingredientList = ingredientList
            fillInputs(viewModel.recipe).also {
                saveRecipe()
            }
        }
        binding.abAdd.setAlpha(false)
    }

    private fun fillInputs(recipe: Recipe?) {
        recipe?.run {
            binding.run {
                name?.let { biRecipe.setInputText(it) }
                volume?.let { biUnit.setInputText(it.toString()) }
                expectedProfit?.let { biExpectedProfit.setInputText(it.toString()) }
            }
        }
    }

    private fun showToast(@StringRes text: Int) {
        CustomToast.Builder(requireContext())
            .message(getString(text))
            .build().show()
    }

    private fun validateFields(): Boolean {
        binding.apply {
            if (biRecipe.getText().isEmpty()) {
                biRecipe.setError()
                return false
            }
            return true
        }
    }

    private fun saveRecipe() {
        binding.run {
            viewModel.addRecipe(
                biRecipe.getText(),
                biUnit.getText().convertToFloat(),
                biExpectedProfit.getText().convertToFloat()
            )
        }
    }

    private fun String.convertToFloat(): Float? {
        try {
            if (this.isEmpty()) {
                return null
            }
            val number = this.toFloat()
            return number
        } catch (_: Exception) {
            return null
        }
    }

}
