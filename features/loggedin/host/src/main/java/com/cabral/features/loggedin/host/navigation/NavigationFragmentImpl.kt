package com.cabral.features.loggedin.host.navigation


import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.core.NavigationFragment
import com.cabral.recipe.presentation.RecipeFragmentDirections as navDirections

class NavigationFragmentImpl : NavigationFragment {
    override fun openFragment(fragment: Fragment) {
        val directions =
           navDirections.actionRecipeToListRecipe()
        fragment.findNavController().navigate(directions)
    }
}