package com.cabral.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

interface ListIngredientNavigation {
    fun openIngredient(fragment: Fragment)
    fun backStackActionHasItemAdd(fragment: Fragment)

    fun hasItemAddOnIngredient(
        fragment: Fragment,
        lifecycleOwner: LifecycleOwner,
        insideFunction: () -> Unit
    )
}