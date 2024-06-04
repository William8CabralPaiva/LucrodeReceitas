package com.cabral.features.loggedin.host.di.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.cabral.arch.Redirection
import com.cabral.core.ListIngredientNavigation
import com.cabral.ingredient.presentation.ListIngredientsFragmentDirections
import com.cabral.model.IngredientArgs

internal class ListIngredientNavigationImpl : ListIngredientNavigation {
    override fun openIngredient(fragment: Fragment, ingredientsArgs: IngredientArgs?) {
        val directions =
            ListIngredientsFragmentDirections.actionListIngredientsToIngredients(ingredientsArgs)
        fragment.findNavController().navigate(directions)
    }

    override fun backStackActionHasItemAdd(fragment: Fragment) {
        fragment.findNavController().run {
            previousBackStackEntry?.savedStateHandle?.set(Redirection.HAS_ADD_ITEM, true)
        }
    }

    override fun hasItemAddOnIngredient(
        fragment: Fragment,
        lifecycleOwner: LifecycleOwner,
        insideFunction: () -> Unit
    ) {
        fragment.findNavController().run {

            val navBackStackEntry = currentBackStackEntry

            val observer = LifecycleEventObserver { _, event ->
                val isSortingApplied =
                    navBackStackEntry?.savedStateHandle?.contains(Redirection.HAS_ADD_ITEM)
                        ?: false

                if (event == Lifecycle.Event.ON_RESUME && isSortingApplied) {
                    insideFunction()

                    navBackStackEntry?.savedStateHandle?.remove<Boolean>(Redirection.HAS_ADD_ITEM)
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)
            lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->

                if (event == Lifecycle.Event.ON_DESTROY) {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }

            })
        }
    }
}