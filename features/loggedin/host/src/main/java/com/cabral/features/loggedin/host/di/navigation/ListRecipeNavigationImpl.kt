package com.cabral.features.loggedin.host.di.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.cabral.core.ListRecipeNavigation
import com.cabral.model.RecipeArgs
import com.cabral.recipe.presentation.ListRecipeFragmentDirections
import com.cabral.recipe.presentation.RecipeFragmentDirections

internal class ListRecipeNavigationImpl : ListRecipeNavigation {

    val SAVE_RECIPE = "SAVE_RECIPE"
    override fun openRecipe(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = ListRecipeFragmentDirections.actionListRecipeToRecipe(recipeArgs)
        fragment.findNavController().navigate(directions)
    }

    override fun backToRecipeFragment(fragment: Fragment, recipeArgs: RecipeArgs?) {
        fragment.findNavController().run {
            previousBackStackEntry?.savedStateHandle?.set(
                SAVE_RECIPE,
                recipeArgs
            )
            popBackStack()
        }
    }

    override fun observeRecipeChangePreviousFragment(
        fragment: Fragment,
        insideFunction: (recipeArgs: RecipeArgs?) -> Unit
    ) {
        val navBackStackEntry =
            fragment.findNavController().getBackStackEntry(com.cabral.recipe.R.id.recipe)

        val observer = LifecycleEventObserver { _, event ->
            val contain = navBackStackEntry.savedStateHandle.contains(SAVE_RECIPE)

            if (event == Lifecycle.Event.ON_RESUME && contain) {
                val recipe =
                    navBackStackEntry.savedStateHandle.get<RecipeArgs>(SAVE_RECIPE)
                insideFunction(recipe)
                navBackStackEntry.savedStateHandle.remove<RecipeArgs>(SAVE_RECIPE)
            }
        }
        fragment.lifecycle.addObserver(observer)
    }

    override fun openAddEditIngredient(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = RecipeFragmentDirections.recipeToAddEditIngredient(recipeArgs)
        fragment.findNavController().navigate(directions)
    }


    override fun openCostsFragment(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = ListRecipeFragmentDirections.actionListRecipeToCostsRecipe(recipeArgs)
        fragment.findNavController().navigate(directions)
    }
}