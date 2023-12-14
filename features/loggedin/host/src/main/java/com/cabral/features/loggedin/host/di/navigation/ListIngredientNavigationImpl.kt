package com.cabral.features.loggedin.host.di.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.cabral.arch.Redirection
import com.cabral.core.ListIngredientNavigation
import com.cabral.listingredients.presentation.ListIngredientsFragmentDirections

internal class ListIngredientNavigationImpl : ListIngredientNavigation {
    override fun openIngredient(fragment: Fragment) {
        val directions = ListIngredientsFragmentDirections.actionListIngredientsToIngredients()
        fragment.findNavController().navigate(directions)
    }

    override fun backStackActionHasItemAdd(fragment: Fragment) {
        fragment.findNavController().run {
            previousBackStackEntry?.savedStateHandle?.set(Redirection.HAS_ADD_ITEM, true)
            popBackStack()
        }
    }

    override fun hasItemAddOnIngredient(fragment: Fragment, lifecycleOwner: LifecycleOwner, insideFunction: () -> Unit) {
        fragment.findNavController().run {

            val observer = LifecycleEventObserver { _, event ->
                val isSortingApplied =
                    currentBackStackEntry?.savedStateHandle?.contains(Redirection.HAS_ADD_ITEM)?:false

                if (event == Lifecycle.Event.ON_RESUME && isSortingApplied) {
                   insideFunction()

                    currentBackStackEntry?.savedStateHandle?.remove<Boolean>(Redirection.HAS_ADD_ITEM)
                }
            }

            currentBackStackEntry?.lifecycle?.addObserver(observer)
            lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->

                if (event == Lifecycle.Event.ON_DESTROY) {
                    currentBackStackEntry?.lifecycle?.removeObserver(observer)
                }

            })
        }
    }
}