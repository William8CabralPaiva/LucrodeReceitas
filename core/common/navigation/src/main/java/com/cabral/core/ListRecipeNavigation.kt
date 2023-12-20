package com.cabral.core

import androidx.fragment.app.Fragment
import com.cabral.model.RecipeArgs

interface ListRecipeNavigation {
    fun openRecipe(fragment: Fragment, recipeArgs: RecipeArgs)
    fun openIngredient(fragment: Fragment)
    fun openAddEditIngredient(fragment: Fragment)
}