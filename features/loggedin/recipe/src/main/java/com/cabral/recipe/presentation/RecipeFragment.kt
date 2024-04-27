package com.cabral.recipe.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cabral.arch.widget.CustomToast
import com.cabral.core.ListRecipeNavigation
import com.cabral.model.toRecipe
import com.cabral.model.toRecipeArgs
import com.cabral.recipe.R
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

    private fun initObservers() {
        viewModel.run {
            notifySuccess.observe(viewLifecycleOwner) {
                binding.abAdd.setAlpha(false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.abAdd.setAlpha(true)
        initObservers()


        if (args.currentRecipe != null) {
            args.currentRecipe?.let {
                viewModel.recipe = it.toRecipe()
            }
        }

        binding.abSave.abSetOnClickListener {

            viewModel.run {
                if (validateFields()) {
                    saveRecipe()
                }
            }
        }

        binding.abAdd.abSetOnClickListener {
            if (viewModel.recipeAlreadyCreate) {
                if (viewModel.recipe.ingredientList == null) {
                    viewModel.recipe.ingredientList = mutableListOf()
                }
                navigation.openAddEditIngredient(this, viewModel.recipe.toRecipeArgs())

            } else {
                showToast(R.string.recipe_save_before_add)
            }

        }

    }

    private fun showToast(@StringRes text: Int) {
        CustomToast.Builder(requireContext())
            .message(getString(text))
            .build().show()
    }

    private fun validateFields(): Boolean {
        binding.apply {
            if (biIngredient.getText().isEmpty()) {
                biIngredient.setError()
                return false
            }
            return true
        }
    }

    private fun saveRecipe() {
        binding.run {
            viewModel.addRecipe(
                biIngredient.getText(),
                biUnit.getText().convertToFloat(),
                biExpectedProfit.getText().convertToFloat()
            )
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
