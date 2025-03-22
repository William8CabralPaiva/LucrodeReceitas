package com.cabral.features.loggedin.host.di.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.cabral.core.ListRecipeNavigation
import com.cabral.model.RecipeArgs

internal class ListRecipeNavigationImpl : ListRecipeNavigation {

    override fun openRecipe(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = bundleOf(CURRENT_RECIPE to recipeArgs)
        fragment.findNavController()
            .navigate(com.cabral.recipe.R.id.action_listRecipe_to_recipe, directions)
    }

    override fun openIngredient(fragment: Fragment) {
        val directions = bundleOf(CURRENT_INGREDIENT to null)
        val id = com.cabral.ingredient.R.id.ingredient_add_nav
        fragment.findNavController().navigate(id, directions)
    }

    override fun backToRecipeFragment(fragment: Fragment, recipeArgs: RecipeArgs?) {
        fragment.findNavController().run {
            previousBackStackEntry?.savedStateHandle?.set(
                SAVE_RECIPE, recipeArgs
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
        val navBackStackEntry = fragment.findNavController().currentBackStackEntry

        val observer = LifecycleEventObserver { _, event ->
            val contain = navBackStackEntry?.savedStateHandle?.contains(key)
            contain?.let {
                if (event == Lifecycle.Event.ON_RESUME && contain) {
                    val obj = navBackStackEntry.savedStateHandle.get<T>(key)
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
                UPDATE_LIST_RECIPE, true
            )
        }
    }

    override fun openAddEditIngredient(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = bundleOf(CURRENT_RECIPE to recipeArgs)
        fragment.findNavController().navigate(
            com.cabral.recipe.R.id.loggedin_hostRecipeaddeditingredientfragment, directions
        )
    }


    override fun openCostsFragment(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = bundleOf(CURRENT_RECIPE to recipeArgs)
        fragment.findNavController()
            .navigate(com.cabral.recipe.R.id.action_list_recipe_to_costs_recipe, directions)
    }

    companion object {
        private const val SAVE_RECIPE = "SAVE_RECIPE"
        private const val UPDATE_LIST_RECIPE = "UPDATE_LIST_RECIPE"
        private const val CURRENT_RECIPE = "currentRecipe"
        private const val CURRENT_INGREDIENT = "currentIngredient"
    }
}