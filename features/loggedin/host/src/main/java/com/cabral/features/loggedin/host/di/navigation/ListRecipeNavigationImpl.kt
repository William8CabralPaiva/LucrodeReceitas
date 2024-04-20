package com.cabral.features.loggedin.host.di.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.core.ListRecipeNavigation
import com.cabral.model.RecipeArgs
import com.cabral.recipe.presentation.ListRecipeFragmentDirections
import com.cabral.recipe.presentation.RecipeFragmentDirections

internal class ListRecipeNavigationImpl : ListRecipeNavigation {
    override fun openRecipe(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = ListRecipeFragmentDirections.actionListRecipeToRecipe(recipeArgs)
        fragment.findNavController().navigate(directions)
    }

    override fun openIngredient(fragment: Fragment) {
        //todo
    }

    override fun openAddEditIngredient(fragment: Fragment, recipeArgs: RecipeArgs?) {
        val directions = RecipeFragmentDirections.recipeToAddEditIngredient(recipeArgs)
        fragment.findNavController().navigate(directions)
    }
}