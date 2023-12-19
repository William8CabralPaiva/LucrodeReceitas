package com.cabral.recipe.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.ListRecipeNavigation
import com.cabral.recipe.databinding.RecipeFragmentBinding
import org.koin.android.ext.android.inject

class RecipeFragment() : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding!!
    //private val activity by lazy { requireActivity() as LoggedActivity }

    private val navigation: ListRecipeNavigation by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.abSave.abSetOnClickListener {
            navigation.openAddEditIngredient(this)
        }

    }


    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(requireContext(), color);
    }

}
