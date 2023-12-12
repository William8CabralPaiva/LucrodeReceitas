package com.cabral.ingredient.presentation

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.cabral.arch.extensions.IngredientThrowable
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.model.allUnitMeasure
import com.cabral.core.common.domain.model.listMeasure
import com.cabral.ingredient.databinding.IngredientsFragmentBinding
import com.cabral.ingredient.presentation.adapter.Adapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class IngredientsFragment : Fragment() {

    private var _binding: IngredientsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IngredientsViewModel by viewModel()

    private lateinit var adapterIngredient: Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IngredientsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObservers()
        initListeners()
        prepareAdapter()
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
            }

        }
    }

    private fun init() {
        val list = listMeasure(allUnitMeasure())
        val adapter = ArrayAdapter(requireContext(), R.layout.select_dialog_item, list)
        binding.biIngredient.setFocus()

        binding.biUnit.getSpinner().run {
            setAdapter(adapter)
            threshold = 1
        }
    }

    private fun initListeners() {
        binding.run {
            binding.abAdd.abSetOnClickListener {
                val volume = biVolume.getText()
                val price = biPrice.getText()
                val unit = biUnit.getSpinner().editableText.toString()
                viewModel.addIngredient(biIngredient.getText(), volume, unit, price)

            }
        }
    }

    private fun prepareAdapter() {
        adapterIngredient = Adapter(requireContext()).apply {
            onClickTrash = {

            }
            onClickEdit = {

            }
        }
        binding.recycleView.adapter = adapterIngredient
        adapterIngredient.submitList(viewModel.listIngredient)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IngredientsFragment()
    }
}