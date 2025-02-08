package com.cabral.recipe.presentation.addeditrecipe

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.cabral.arch.BaseFragment
import com.cabral.arch.extensions.collectIn
import com.cabral.arch.extensions.removeEndZero
import com.cabral.arch.widget.BorderInputView
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.arch.widget.CustomToast
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.ListRecipeNavigation
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.design.R
import com.cabral.model.toRecipe
import com.cabral.model.toRecipeArgs
import com.cabral.recipe.adapter.IngredientAdapter
import com.cabral.recipe.databinding.RecipeAddEditIngredientFragmentBinding
import com.google.android.gms.ads.AdRequest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class RecipeAddEditIngredientFragment :
    BaseFragment<RecipeAddEditIngredientFragmentBinding>(RecipeAddEditIngredientFragmentBinding::inflate) {

    private lateinit var ingredientAdapterIngredient: IngredientAdapter

    private val viewModel: RecipeAddEditIngredientViewModel by viewModel()

    private val args: RecipeAddEditIngredientFragmentArgs by navArgs()

    private val navigation: ListRecipeNavigation by inject()

    private val navigationIngredient: ListIngredientNavigation by inject()

    override fun onResume() {
        super.onResume()
        viewModel.getAllIngredients()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initArgs()
        initListeners()
        initGlide()
        initAds()
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun initGlide() {
        Glide.with(requireActivity()).asGif()
            .load(com.cabral.recipe.R.drawable.listrecipe_hand_down)
            .into(binding.ivHand)

    }

    private fun initArgs() {
        args.currentRecipe?.let {
            viewModel.recipe = it.toRecipe()
        }
    }

    private fun initObservers() {

        viewModel.uiEvent.collectIn(this) {
            if (it is UiEvent.ShowToast) {
                showToast(it.text)
            }
        }

        viewModel.uiState.collectIn(this) {
            when (it) {
                is UiState.Default -> Unit
                is UiState.Error -> binding.viewFlipper.displayedChild = 2
                is UiState.ErrorSpinner -> binding.biIngredient.setError()
                is UiState.DisableSpinner -> disableSpinner()
                is UiState.EditMode -> editMode(it.editMode)
                is UiState.RemoveIngredient -> ingredientAdapterIngredient.notifyItemRemoved(it.id)
                is UiState.SuccessEdit -> ingredientAdapterIngredient.notifyItemChanged(it.position)
                is UiState.AddIngredient -> addIngredient(it.ingredient?.id)
                is UiState.ListIngredient -> showList(it.list)
            }
        }
    }

    private fun addIngredient(id: Int?) {
        id?.let {
            ingredientAdapterIngredient.notifyItemInserted(id)
            clearFields()
        }
    }

    private fun showList(list: List<Ingredient?>) {
        binding.viewFlipper.displayedChild = 1
        initAdapter(list.getIngredientName())
        binding.ivHand.isVisible = false
    }

    private fun editMode(editMode: Boolean) {
        val buttonText = if (editMode) {
            getString(DesignR.string.design_edit)
        } else {
            binding.biIngredient.clearInputText()
            binding.biVolume.clearInputText()
            getString(DesignR.string.design_add)
        }
        binding.abAdd.setText(buttonText)
    }

    private fun disableSpinner() {
        binding.viewFlipper.displayedChild = 1
        binding.biIngredient.setError()
        binding.biIngredient.setError(getString(DesignR.string.design_ingredients_register))
        binding.ivHand.isVisible = true
    }

    private fun initAdapter(list: MutableList<String?>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, list)

        binding.biIngredient.getSpinner().run {
            setAdapter(adapter)
            threshold = 1
        }
        prepareAdapter()
    }

    private fun clearFields() {
        binding.run {
            biIngredient.clearInputText()
            txtUnit.visibility = View.INVISIBLE
            biVolume.clearInputText()
        }
    }

    private fun initListeners() {

        binding.biIngredient.getSpinner().addTextChangedListener { ed ->
            val list = viewModel.listAllIngredients.getIngredientName()
            if (list.contains(ed.toString())) {
                val ingredient =
                    viewModel.listAllIngredients.first { ingredient -> ingredient?.name == ed.toString() }
                binding.txtUnit.run {
                    text = ingredient?.unit
                    visibility = View.VISIBLE
                }
            } else {
                binding.txtUnit.visibility = View.INVISIBLE
            }
            binding.biVolume.enableButtonVolume()
        }

        binding.biVolume.setTextChangedListener {
            binding.biVolume.enableButtonVolume()
        }

        binding.abAdd.abSetOnClickListener {
            if (binding.biVolume.getText().isNotEmpty()) {
                viewModel.addIngredientInList(
                    binding.biIngredient.getText(), binding.biVolume.getText().toFloat()
                )
            }
        }

        binding.abSave.abSetOnClickListener {
            navigation.backToRecipeFragment(
                this@RecipeAddEditIngredientFragment, viewModel.recipe.toRecipeArgs()
            )
        }

        binding.abAddIngredient.abSetOnClickListener {
            navigation.openIngredient(this)
        }

        navigationIngredient.hasItemAddOnIngredient(this, viewLifecycleOwner) {
            viewModel.getAllIngredients()
        }
    }

    private fun BorderInputView.enableButtonVolume() {
        binding.abAdd.run {
            try {
                if (getText().isNotEmpty() && getText().toFloat() > 0 && !binding.biIngredient.getSpinner().text.isNullOrEmpty()) {
                    abSetEnabled(true)

                } else {
                    abSetEnabled(false)
                }
            } catch (_: Exception) {
                abSetEnabled(false)
            }
        }
    }

    private fun List<Ingredient?>.getIngredientName(): MutableList<String?> {
        val list = mutableListOf<String?>()
        forEach {
            it?.name?.let { it1 -> list.add(it1) }
        }
        return list
    }

    private fun prepareAdapter() {
        ingredientAdapterIngredient = IngredientAdapter(requireContext()).apply {
            onClickTrash = {
                it.name?.let { name ->
                    if (!viewModel.getEditMode()) {
                        showAlertDialog(
                            DesignR.string.design_delete_title,
                            DesignR.string.design_delete_message,
                            name
                        ) { viewModel.deleteItemAdd(it) }
                    } else {
                        showToast(DesignR.string.design_delete_in_edit)
                    }

                }
            }
            onClickEdit = {
                it.name?.let { name ->
                    showAlertDialog(
                        DesignR.string.design_edit_title, DesignR.string.design_edit_message, name
                    ) { it.editItem() }
                }
            }
        }
        binding.recycleView.adapter = ingredientAdapterIngredient
        ingredientAdapterIngredient.submitList(viewModel.listAddIngredients)
    }

    private fun showToast(@StringRes text: Int) {
        CustomToast.Builder(requireContext())
            .message(getString(text))
            .build().show()
    }

    private fun Ingredient.editItem() {
        setAllInputsText()
        viewModel.setEditMode(true, this)
    }

    private fun Ingredient.setAllInputsText() {
        this.name
        binding.run {
            name?.let { name ->
                biIngredient.setInputText(name)
            }

            biVolume.setInputText(volume.removeEndZero())
        }
    }

    private fun showAlertDialog(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        item: String,
        positiveFunction: () -> Unit
    ) {
        context?.let {
            val title = String.format(getString(titleRes), item)
            val message = String.format(getString(messageRes), item)
            CustomAlertDialog.Builder(requireContext())
                .title(title)
                .message(message)
                .negativeButton(getString(R.string.design_no))
                .positiveButton(getString(R.string.design_yes)).positiveFunction {
                    positiveFunction()
                }

                .build().show()
        }
    }

}