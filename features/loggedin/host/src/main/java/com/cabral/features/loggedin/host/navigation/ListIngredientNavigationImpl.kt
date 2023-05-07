package com.cabral.features.loggedin.host.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.core.ListIngredientNavigation
import com.cabral.listingredients.presentation.ListIngredientsFragmentDirections

internal class ListIngredientNavigationImpl : ListIngredientNavigation {
    override fun openIngredient(fragment: Fragment) {
        val directions = ListIngredientsFragmentDirections.actionListIngredientsToIngredients()
        fragment.findNavController().navigate(directions)
    }
}