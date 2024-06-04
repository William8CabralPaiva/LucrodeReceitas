package com.cabral.ingredient.presentation

import android.R
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cabral.arch.BaseFragment
import com.cabral.arch.extensions.IngredientThrowable
import com.cabral.arch.extensions.removeEndZero
import com.cabral.arch.widget.CustomAlertDialog
import com.cabral.arch.widget.CustomToast
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.allUnitMeasure
import com.cabral.core.common.domain.model.listMeasure
import com.cabral.ingredient.databinding.IngredientsFragmentBinding
import com.cabral.ingredient.presentation.adapter.IngredientAdapter
import com.cabral.model.toIngredient
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class IngredientsFragment :
    BaseFragment<IngredientsFragmentBinding>(IngredientsFragmentBinding::inflate) {

    private val viewModel: IngredientsViewModel by viewModel()

    private lateinit var adapterIngredient: IngredientAdapter

    private val navigationIngredient: ListIngredientNavigation by inject()

    private val args: IngredientsFragmentArgs? by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initAdapter()
        initAds()

        prepareLoad()
        initObservers()
        initListeners()
    }

    private fun initArgs() {
        try {
            args?.currentIngredient?.let {
                viewModel.editExistItem = true
                val ingredient = it.toIngredient()
                ingredient.setAllInputsText()
                viewModel.changeIngredient(ingredient)
            }
        } catch (_: Exception) {
        }
    }

    private fun prepareLoad() {
        navigationIngredient.backStackActionHasItemAdd(this@IngredientsFragment)
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun initObservers() {
        viewModel.run {
            notifyErrorAdd.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    when (it) {
                        is IngredientThrowable.NameThrowable -> {
                            binding.biIngredient.setError()
                        }

                        is IngredientThrowable.UnitThrowable -> {
                            binding.biUnit.setError(getString(DesignR.string.design_select_field))
                        }

                        is IngredientThrowable.SizeThrowable -> {
                            binding.biVolume.setError(getString(DesignR.string.design_required_field))
                        }

                        else -> {
                            binding.biPrice.setError()
                        }
                    }
                }
            }

            notifySuccessAdd.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    adapterIngredient.notifyItemInserted(it)
                    clearFields()
                    binding.biIngredient.setFocus()
                }
            }

            notifySuccessEdit.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    adapterIngredient.notifyItemChanged(it)

                    clearFields()
                }
            }

            notifyEditMode.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    val buttonText = if (it) {
                        getString(DesignR.string.design_edit)
                    } else {
                        getString(DesignR.string.design_add)
                    }
                    binding.abAdd.setText(buttonText)
                }
            }

            notifySuccess.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    findNavController().popBackStack()
                }
            }

            notifyError.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    context?.run {
                        val text = getString(DesignR.string.design_error_check_your_connection)
                        CustomToast.Builder(requireContext())
                            .message(text)
                            .setBackgroundColor(DesignR.color.design_dark_red)
                            .build().show()
                    }
                }
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
                val price = biPrice.getRawText()
                val unit = biUnit.getSpinner().editableText.toString()
                viewModel.addOrEditIngredient(biIngredient.getText(), volume, unit, price)
            }

            abSave.abSetOnClickListener {
                if (viewModel.listIngredient.isNotEmpty()) {
                    viewModel.save()
                } else {
                    showToast(DesignR.string.design_empty_list)
                }
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
                        showToast(DesignR.string.design_delete_in_edit)
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

    private fun showToast(@StringRes text: Int) {
        CustomToast.Builder(requireContext())
            .message(getString(text))
            .build().show()
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

}