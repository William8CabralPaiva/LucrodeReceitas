package com.cabral.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.cabral.model.IngredientArgs

interface ListIngredientNavigation {
    fun openIngredient(fragment: Fragment, ingredientsArgs: IngredientArgs? = null)
    fun backStackActionHasItemAdd(fragment: Fragment)

    fun hasItemAddOnIngredient(
        fragment: Fragment,
        lifecycleOwner: LifecycleOwner,
        insideFunction: () -> Unit
    )
}