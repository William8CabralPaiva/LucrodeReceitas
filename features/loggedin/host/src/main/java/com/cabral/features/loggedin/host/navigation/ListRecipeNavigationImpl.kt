package com.cabral.features.loggedin.host.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.core.ListRecipeNavigation
import com.cabral.listrecipe.presentation.ListRecipeFragmentDirections

class ListRecipeNavigationImpl : ListRecipeNavigation {
    override fun openRecipe(fragment: Fragment) {
        val directions = ListRecipeFragmentDirections.actionListRecipeToRecipe()
        fragment.findNavController().navigate(directions)
    }
}