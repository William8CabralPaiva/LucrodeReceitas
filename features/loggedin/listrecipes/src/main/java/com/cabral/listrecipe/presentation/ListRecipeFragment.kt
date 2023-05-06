package com.cabral.listrecipe.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.cabral.arch.SwipeGesture
import com.cabral.core.ListRecipeNavigation
import com.cabral.core.common.domain.model.Recipe
import com.cabral.listrecipe.databinding.FragmentListRecipeBinding
import com.cabral.listrecipe.presentation.adapter.Adapter
import org.koin.android.ext.android.inject

class ListRecipeFragment : Fragment() {

    private var _binding: FragmentListRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Adapter

    private val navigationRecipe: ListRecipeNavigation by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewFlipper.displayedChild = 1
        initClicks()
        initRecycleView()
    }

    private fun initClicks() {
        binding.addRecipe.setOnClickListener {
            navigationRecipe.openRecipe(this)
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

        val r = Recipe(0, "receita 1", 50.00f)
        val r2 = Recipe(0, "receita 2", 25.10f)

        val list = listOf(r, r2, r, r2, r, r2, r, r2, r, r2, r, r2, r, r2)

        adapter.submitList(list)

        val dragDrop = SwipeGesture(list)
        val itemTouchHelper = ItemTouchHelper(dragDrop)
        itemTouchHelper.attachToRecyclerView(binding.recycleView)
    }

}