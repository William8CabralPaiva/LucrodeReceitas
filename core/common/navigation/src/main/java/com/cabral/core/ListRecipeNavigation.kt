package com.cabral.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.cabral.model.RecipeArgs

interface ListRecipeNavigation {
    fun openRecipe(fragment: Fragment, recipeArgs: RecipeArgs?)
    fun openIngredient(fragment: Fragment)
    fun backToRecipeFragment(fragment: Fragment, recipeArgs: RecipeArgs?)

    fun observeRecipeChangeOnRecipeAddEditFragment(
        fragment: Fragment,
        lifecycleOwner: LifecycleOwner,
        insideFunction: (recipeArgs: RecipeArgs?) -> Unit
    )

    fun observeRecipeChangeOnListRecipeFragment(
        fragment: Fragment,
        lifecycleOwner: LifecycleOwner,
        insideFunction: (hasChanged: Boolean?) -> Unit
    )

    fun observeListRecipeHasChanged(
        fragment: Fragment
    )

    fun openAddEditIngredient(fragment: Fragment, recipeArgs: RecipeArgs?)

    fun openCostsFragment(fragment: Fragment, recipeArgs: RecipeArgs?)
}