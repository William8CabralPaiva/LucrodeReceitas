package com.cabral.recipe.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cabral.arch.BaseFragment
import com.cabral.arch.extensions.removeEndZero
import com.cabral.arch.widget.BorderInputView
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.arch.widget.CustomToast
import com.cabral.core.ListRecipeNavigation
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.design.R
import com.cabral.model.RecipeArgs
import com.cabral.model.toRecipe
import com.cabral.model.toRecipeArgs
import com.cabral.recipe.adapter.IngredientAdapter
import com.cabral.recipe.databinding.RecipeAddEditIngredientFragmentBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class RecipeAddEditIngredientFragment :
    BaseFragment<RecipeAddEditIngredientFragmentBinding>(RecipeAddEditIngredientFragmentBinding::inflate) {

    private lateinit var ingredientAdapterIngredient: IngredientAdapter

    private val viewModel: RecipeAddEditIngredientFragmentViewModel by viewModel()

    private val args: RecipeAddEditIngredientFragmentArgs by navArgs()

    private val navigation: ListRecipeNavigation by inject()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initArgs()
        initOnBackPress()
    }

    private fun initArgs() {
        args.currentRecipe?.let {
            viewModel.recipe = it.toRecipe()
        }
    }

    private fun initOnBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                navigation.backToRecipeFragment(
                    this@RecipeAddEditIngredientFragment, null
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun initObservers() {
        viewModel.run {

            notifyError.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 2
            }

            notifyListIngredient.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 1
                it?.let {
                    initAdapter(it.getIngredientName())
                }
            }

            notifyAddIngredient.observe(viewLifecycleOwner) {
                it?.id?.let { ingredient ->
                    ingredientAdapterIngredient.notifyItemInserted(ingredient)
                    clearFields()
                }
            }

            notifyEditMode.observe(viewLifecycleOwner) {
                val buttonText = if (it) {
                    getString(DesignR.string.design_edit)
                } else {
                    getString(DesignR.string.design_add)
                }
                binding.abAdd.setText(buttonText)
            }

            notifyShowToast.observe(viewLifecycleOwner) {
                showToast(it)
            }

            notifyRemoveIngredient.observe(viewLifecycleOwner) {
                ingredientAdapterIngredient.notifyItemRemoved(it)
            }

            notifySuccessEdit.observe(viewLifecycleOwner) {
                ingredientAdapterIngredient.notifyItemChanged(it)
            }
        }
    }

    private fun initAdapter(list: MutableList<String?>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, list)

        binding.biIngredient.getSpinner().run {
            setAdapter(adapter)
            threshold = 1
        }
        prepareAdapter()
        initListeners()
    }

    private fun clearFields() {
        binding.run {
            biIngredient.clearInputText()
            txtUnit.visibility = View.INVISIBLE
            biVolume.clearInputText()
        }
    }

    private fun initListeners() {

        val list = viewModel.listAllIngredients.getIngredientName()
        binding.biIngredient.getSpinner().addTextChangedListener { ed ->
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

        binding.addIngredient.abSetOnClickListener {
            navigation.openIngredient(this)
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