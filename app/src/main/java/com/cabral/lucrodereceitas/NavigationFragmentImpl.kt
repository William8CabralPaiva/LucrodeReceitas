package com.cabral.lucrodereceitas

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.core.NavigationFragment
import com.cabral.recipe.presentation.RecipeFragmentDirections

class NavigationFragmentImpl : NavigationFragment {
    override fun openFragment(fragment: Fragment) {
        val directions = RecipeFragmentDirections.actionRecipeToListRecipeFragment2()
        fragment.findNavController().navigate(directions)
    }
}