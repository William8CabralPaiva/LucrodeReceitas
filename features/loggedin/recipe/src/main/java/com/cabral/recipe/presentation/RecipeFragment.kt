package com.cabral.recipe.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cabral.arch.extensions.RecipeThrowable
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.ListRecipeNavigation
import com.cabral.model.toRecipe
import com.cabral.model.toRecipeArgs
import com.cabral.recipe.databinding.RecipeFragmentBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeFragment() : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding!!
    //private val activity by lazy { requireActivity() as LoggedActivity }

    private val navigation: ListRecipeNavigation by inject()

    private val args: RecipeFragmentArgs by navArgs()

    private val viewModel: RecipeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.currentRecipe != null) {
            args.currentRecipe?.let {
                viewModel.recipe = it.toRecipe()
            }
        }

        binding.abSave.abSetOnClickListener {
            if (viewModel.recipe.ingredientList == null) {
                viewModel.recipe.ingredientList = mutableListOf()
            }
            navigation.openAddEditIngredient(this, viewModel.recipe.toRecipeArgs())
        }

        binding.abAdd.abSetOnClickListener {
            if (args.currentRecipe != null || viewModel.recipeAlreadyCreate) {

            } else {
                binding.run {
                    viewModel.addRecipe(
                        biIngredient.getText(),
                        biUnit.getText().convertToFloat(),
                        biExpectedProfit.getText().convertToFloat()
                    )
                }
            }
        }

    }

    private fun String.convertToFloat(): Float? {
        try {
            if (this.isEmpty()) {
                return null
            }
            val number = this.toFloat()
            return number
        } catch (_: Exception) {
            return null
        }
    }


    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(requireContext(), color);
    }

}
