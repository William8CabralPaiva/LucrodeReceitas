package com.cabral.recipe.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
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

    private var updateIngredients = false

    private fun initObservers() {
        viewModel.run {
            notifySuccess.observe(viewLifecycleOwner) {
                binding.abAdd.setAlpha(false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.abAdd.setAlpha(true)
        initObservers()

//        val navBackStackEntry =
//            findNavController().getBackStackEntry(com.cabral.recipe.R.id.recipe)
//
//        val observer = LifecycleEventObserver { _, event ->
//
//            val contain =navBackStackEntry.savedStateHandle.contains("SAVE_RECIPE")
//
//            if (event == Lifecycle.Event.ON_RESUME && contain) {
//                val isSortingApplied =
//                    navBackStackEntry.savedStateHandle.get<RecipeArgs>("SAVE_RECIPE")
//                viewModel.recipe.ingredientList = isSortingApplied?.toRecipe()?.ingredientList
//                navBackStackEntry.savedStateHandle.remove<RecipeArgs>("SAVE_RECIPE")
//            }
//        }
//
//
//        navBackStackEntry.lifecycle.addObserver(observer)

        navigation.observeRecipeChangePreviousFragment(this) { recipe ->
            viewModel.recipe.ingredientList = recipe?.toRecipe()?.ingredientList
            updateIngredients = true
        }

        if (args.currentRecipe != null) {
            args.currentRecipe?.let {
                if(!updateIngredients) {
                    viewModel.recipe = it.toRecipe()
                }else{
                    updateIngredients = false
                }
                viewModel.recipeAlreadyCreate = true
                binding.abAdd.setAlpha(false)

                fillInputs(viewModel.recipe)
            }
        }

        binding.abSave.abSetOnClickListener {

            viewModel.run {
                if (validateFields()) {
                    saveRecipe()
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

    private fun fillInputs(recipe: Recipe?) {
        recipe?.run {
            binding.run {
                name?.let { biIngredient.setInputText(it) }
                volumeUnit?.let { biUnit.setInputText(it.toString()) }
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
            if (biIngredient.getText().isEmpty()) {
                biIngredient.setError()
                return false
            }
            return true
        }
    }

    private fun saveRecipe() {
        binding.run {
            viewModel.addRecipe(
                biIngredient.getText(),
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


    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(requireContext(), color);
    }

}
