package com.cabral.listingredients.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cabral.core.ListIngredientNavigation
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.listingredients.databinding.ListingredientsFragmentBinding
import com.cabral.listingredients.presentation.adapter.Adapter
import org.koin.android.ext.android.inject

class ListIngredientsFragment : Fragment() {

    private var _binding: ListingredientsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Adapter

    private val navigationIngredient: ListIngredientNavigation by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListingredientsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initClicks()
        binding.viewFlipper.displayedChild = 1
    }


    private fun initClicks() {
        binding.addIngredient.setOnClickListener {
            navigationIngredient.openIngredient(this)
        }
    }

    private fun initRecycleView() {
        adapter = Adapter(requireContext()).apply {
            onClick = {
                Toast.makeText(requireContext(), it.name, Toast.LENGTH_LONG).show()
            }
            onClickTrash = {
                Toast.makeText(requireContext(), it.name, Toast.LENGTH_LONG).show()
            }
        }

        binding.recycleView.adapter = adapter

        val r = Ingredient(0, "Farinha", 20f, "1 Kg", 20f)
        val r2 = Ingredient(0, "Ovo", 20f, "12 unidades", 40f)

        val list = listOf(r, r2, r, r2, r, r2, r, r2, r, r2, r, r2, r, r2)

        adapter.submitList(list)

    }

}