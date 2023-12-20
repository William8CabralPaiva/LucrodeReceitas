package com.cabral.ingredient.presentation

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.cabral.arch.extensions.IngredientThrowable
import com.cabral.arch.extensions.removeEndZero
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.allUnitMeasure
import com.cabral.core.common.domain.model.listMeasure
import com.cabral.ingredient.databinding.IngredientsFragmentBinding
import com.cabral.ingredient.presentation.adapter.IngredientAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR


class IngredientsFragment : Fragment() {

    private var _binding: IngredientsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IngredientsViewModel by viewModel()

    private lateinit var adapterIngredient: IngredientAdapter

    private val navigationIngredient: ListIngredientNavigation by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IngredientsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.run {
            notifyErrorAdd.observe(viewLifecycleOwner) {
                when (it) {
                    is IngredientThrowable.NameThrowable -> {
                        binding.biIngredient.setError(it.message)
                    }

                    is IngredientThrowable.UnitThrowable -> {
                        binding.biUnit.setError(it.message)
                    }

                    is IngredientThrowable.SizeThrowable -> {
                        binding.biVolume.setError(it.message)
                    }

                    else -> {
                        binding.biPrice.setError(it.message)
                    }
                }
            }

            notifySuccessAdd.observe(viewLifecycleOwner) {
                adapterIngredient.notifyItemInserted(it)
                clearFields()
            }

            notifySuccessEdit.observe(viewLifecycleOwner) {
                it?.let {
                    adapterIngredient.notifyItemChanged(it)
                }
                clearFields()
            }

            notifyEditMode.observe(viewLifecycleOwner) {
                val buttonText = if (it) {
                    getString(DesignR.string.design_edit)
                } else {
                    getString(DesignR.string.design_add)
                }
                binding.abAdd.setText(buttonText)
            }

            notifySuccess.observe(viewLifecycleOwner){
                navigationIngredient.backStackActionHasItemAdd(this@IngredientsFragment)
            }

        }
    }

    private fun initAdapter() {
        val list = listMeasure(allUnitMeasure())
        val adapter = ArrayAdapter(requireContext(), R.layout.select_dialog_item, list)
        binding.biIngredient.setFocus()

        binding.biUnit.getSpinner().run {
            setAdapter(adapter)
            threshold = 1
        }
        prepareAdapter()
    }

    private fun initListeners() {
        binding.run {
            abAdd.abSetOnClickListener {
                val volume = biVolume.getText()
                val price = biPrice.getText()
                val unit = biUnit.getSpinner().editableText.toString()
                viewModel.addOrEditIngredient(biIngredient.getText(), volume, unit, price)
            }

            abSave.abSetOnClickListener {
                viewModel.save()
            }

        }
    }

    private fun prepareAdapter() {
        adapterIngredient = IngredientAdapter(requireContext()).apply {
            onClickTrash = {
                it.name?.let { name ->
                    if (!viewModel.getEditMode()) {
                        showAlertDialog(
                            DesignR.string.design_delete_title,
                            DesignR.string.design_delete_message,
                            name
                        ) { it.deleteItem() }
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
        binding.recycleView.adapter = adapterIngredient
        adapterIngredient.submitList(viewModel.listIngredient)
    }

    private fun disableAllInput(){
        binding.run {
            //biIngredient.
        }
    }

    private fun Ingredient.deleteItem() {
        viewModel.listIngredient.remove(this)
        val position = viewModel.listIngredient.indexOf(this)
        adapterIngredient.notifyItemRemoved(position)
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

            unit?.let { unit ->
                biUnit.setInputText(unit)
            }

            biPrice.setInputText(price.removeEndZero())
            biVolume.setInputText(volume.removeEndZero())
        }
    }

    private fun clearFields() {
        binding.run {
            biIngredient.clearInputText()
            biPrice.clearInputText()
            biVolume.clearInputText()
            biUnit.clearInputText()
        }
    }

    private fun showAlertDialog(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        item: String,
        positiveFunction: () -> Unit
    ) {
        context?.let {
            val message = String.format(getString(messageRes), item)
            CustomAlertDialog.Builder(requireContext())
                .title(getString(titleRes))
                .message(message)
                .negativeButton(getString(DesignR.string.design_no))
                .positiveButton(getString(DesignR.string.design_yes))
                .positiveFunction {
                    positiveFunction()
                }

                .build().show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}