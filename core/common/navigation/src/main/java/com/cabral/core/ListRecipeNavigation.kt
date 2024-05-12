package com.cabral.core

import androidx.fragment.app.Fragment
import com.cabral.model.RecipeArgs

interface ListRecipeNavigation {
    fun openRecipe(fragment: Fragment, recipeArgs: RecipeArgs?)
    fun backToRecipeFragment(fragment: Fragment, recipeArgs: RecipeArgs?)

    fun observeRecipeChangePreviousFragment(
        fragment: Fragment,
        insideFunction: (recipeArgs: RecipeArgs?) -> Unit
    )

    fun openAddEditIngredient(fragment: Fragment, recipeArgs: RecipeArgs?)

    fun openCostsFragment(fragment: Fragment, recipeArgs: RecipeArgs?)
}