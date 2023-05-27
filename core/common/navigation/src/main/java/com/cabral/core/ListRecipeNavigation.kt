package com.cabral.core

import androidx.fragment.app.Fragment

interface ListRecipeNavigation {
    fun openRecipe(fragment: Fragment)
    fun openIngredient(fragment: Fragment)
}