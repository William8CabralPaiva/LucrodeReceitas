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
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.listrecipe.databinding.ListrecipeFragmentBinding
import com.cabral.listrecipe.presentation.adapter.Adapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListRecipeFragment : Fragment() {

    private var _binding: ListrecipeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Adapter

    private val navigationRecipe: ListRecipeNavigation by inject()

    private val viewModel: ListRecipeViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListrecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        initObservers()
    }

    private fun initObservers() {
        viewModel.run {

            notifyStartLoading.observe(viewLifecycleOwner){
                binding.viewFlipper.displayedChild = 0
            }

            notifyEmptyList.observe(viewLifecycleOwner){
                binding.viewFlipper.displayedChild = 1
            }

            notifyListRecipe.observe(viewLifecycleOwner){
                binding.viewFlipper.displayedChild = 2
                initAdapter()
            }

        }
    }

    private fun initAdapter() {
        adapter = Adapter(requireContext()).apply {
            onClick = {
                Toast.makeText(requireContext(), it.name, Toast.LENGTH_LONG).show()
            }
            onClickTrash = {
//                it.name?.let { it1 ->
//                    showAlertDialog(
//                        ListIngredientR.string.listingredients_remove_modal_title,
//                        ListIngredientR.string.listingredients_remove_warning,
//                        it1
//                    ){
//                        viewModel.deleteIngredient(it)
//                    }
//                }
            }
        }

        binding.recycleView.adapter = adapter
        adapter.submitList(viewModel.listRecipe)
    }

    private fun initClicks() {
        binding.addRecipe.setOnClickListener {
            navigationRecipe.openRecipe(this)
        }
    }

//    private fun initRecycleView() {
//        adapter = Adapter(requireContext()).apply {
//            onClick = {
//                Toast.makeText(requireContext(), it.name, Toast.LENGTH_LONG).show()
//            }
//            onClickTrash = {
//                Toast.makeText(requireContext(), it.name, Toast.LENGTH_LONG).show()
//            }
//        }
//
//        binding.recycleView.adapter = adapter
//
//        val r = Recipe(0, "receita 1", 50.00f)
//        val r2 = Recipe(0, "receita 2", 25.10f)
//
//        val list = listOf(r, r2, r, r2, r, r2, r, r2, r, r2, r, r2, r, r2)
//
//        adapter.submitList(list)
//
//        val dragDrop = SwipeGesture(list)
//        val itemTouchHelper = ItemTouchHelper(dragDrop)
//        itemTouchHelper.attachToRecyclerView(binding.recycleView)
//    }

}