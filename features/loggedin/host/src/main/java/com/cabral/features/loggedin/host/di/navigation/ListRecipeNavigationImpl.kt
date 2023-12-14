package com.cabral.features.loggedin.host.di.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.core.ListRecipeNavigation
import com.cabral.listrecipe.presentation.ListRecipeFragmentDirections

internal class ListRecipeNavigationImpl : ListRecipeNavigation {
    override fun openRecipe(fragment: Fragment) {
        val directions = ListRecipeFragmentDirections.actionListRecipeToRecipe()
        fragment.findNavController().navigate(directions)
    }

    override fun openIngredient(fragment: Fragment) {
        //todo
    }
}