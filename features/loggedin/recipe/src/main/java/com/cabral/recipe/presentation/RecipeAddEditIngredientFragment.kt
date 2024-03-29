package com.cabral.recipe.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import com.cabral.arch.extensions.removeEndZero
import com.cabral.arch.widget.BorderInputView
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.design.R
import com.cabral.recipe.adapter.IngredientAdapter
import com.cabral.recipe.databinding.RecipeAddEditIngredientFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class RecipeAddEditIngredientFragment : Fragment() {

    private var _binding: RecipeAddEditIngredientFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientAdapterIngredient: IngredientAdapter

    private val viewModel: RecipeAddEditIngredientFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipeAddEditIngredientFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.run {
            notifyListIngredient.observe(viewLifecycleOwner) {
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

            notifySuccessEdit.observe(viewLifecycleOwner){
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
        val list = viewModel.listIngredient.getIngredientName()
        binding.biIngredient.getSpinner().addTextChangedListener { ed ->
            if (list.contains(ed.toString())) {
                val ingredient =
                    viewModel.listIngredient.first { ingredient -> ingredient?.name == ed.toString() }
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
                    binding.biIngredient.getText(),
                    binding.biVolume.getText().toFloat()
                )
            }
        }
    }

    private fun showToast(@StringRes text: Int) {
        Toast.makeText(requireContext(), getString(text), Toast.LENGTH_LONG).show()
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
                        Toast.makeText(
                            context,
                            getString(DesignR.string.design_delete_in_edit),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }
            onClickEdit = {
                it.name?.let { name ->
                    showAlertDialog(
                        DesignR.string.design_edit_title,
                        DesignR.string.design_edit_message,
                        name
                    ) { it.editItem() }
                }
            }
        }
        binding.recycleView.adapter = ingredientAdapterIngredient
        ingredientAdapterIngredient.submitList(viewModel.addListIngredient)
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
                .positiveButton(getString(R.string.design_yes))
                .positiveFunction {
                    positiveFunction()
                }

                .build().show()
        }
    }

}