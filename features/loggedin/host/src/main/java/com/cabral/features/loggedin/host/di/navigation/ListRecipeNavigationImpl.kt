package com.cabral.features.loggedin.host.di.navigation

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.cabral.core.ListRecipeNavigation
import com.cabral.features.loggedin.host.R
import com.cabral.model.RecipeArgs
import com.cabral.recipe.presentation.ListRecipeFragmentDirections
import com.cabral.recipe.presentation.RecipeFragmentDirections

internal class ListRecipeNavigationImpl : ListRecipeNavigation {

    val SAVE_RECIPE = "SAVE_RECIPE"
    val UPDATE_LIST_RECIPE = "UPDATE_LIST_RECIPE"

    override fun openRecipe(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = ListRecipeFragmentDirections.actionListRecipeToRecipe(recipeArgs)
        fragment.findNavController().navigate(directions)
    }

    override fun openIngredient(fragment: Fragment) {
        val id = com.cabral.ingredient.R.id.ingredient_add_nav
        fragment.findNavController().navigate(id)
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

    override fun observeRecipeChangeOnRecipeAddEditFragment(
        fragment: Fragment,
        lifecycleOwner: LifecycleOwner,
        insideFunction: (recipeArgs: RecipeArgs?) -> Unit
    ) {
        backScreenAction(fragment, insideFunction, lifecycleOwner, SAVE_RECIPE)
    }

    override fun observeRecipeChangeOnListRecipeFragment(
        fragment: Fragment,
        lifecycleOwner: LifecycleOwner,
        insideFunction: (hasChanged: Boolean?) -> Unit
    ) {
        backScreenAction(fragment, insideFunction, lifecycleOwner, UPDATE_LIST_RECIPE)
    }


    private fun <T> backScreenAction(
        fragment: Fragment,
        insideFunction: (objectParam: T?) -> Unit,
        lifecycleOwner: LifecycleOwner,
        key: String
    ) {
        val navBackStackEntry =
            fragment.findNavController().currentBackStackEntry

        val observer = LifecycleEventObserver { _, event ->
            val contain = navBackStackEntry?.savedStateHandle?.contains(key)
            contain?.let {
                if (event == Lifecycle.Event.ON_RESUME && contain) {
                    val obj =
                        navBackStackEntry.savedStateHandle.get<T>(key)
                    navBackStackEntry.savedStateHandle.remove<T>(key)
                    insideFunction(obj)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_DESTROY) {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }

        })
    }

    override fun observeListRecipeHasChanged(fragment: Fragment) {
        fragment.findNavController().run {
            previousBackStackEntry?.savedStateHandle?.set(
                UPDATE_LIST_RECIPE,
                true
            )
        }
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