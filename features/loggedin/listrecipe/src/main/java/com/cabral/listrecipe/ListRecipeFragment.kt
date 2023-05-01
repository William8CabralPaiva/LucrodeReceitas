package com.cabral.listrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cabral.listrecipe.adapter.Adapter
import com.cabral.listrecipe.data.Recipe
import com.cabral.listrecipe.databinding.FragmentListRecipeBinding

class ListRecipeFragment : Fragment() {

    private var _binding: FragmentListRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.viewFlipper.displayedChild = 1

        initRecycleView()
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

        val a = listOf(r, r2)

        adapter.submitList(a)
    }

}