package com.cabral.lucrodereceitas.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.lucrodereceitas.LoggedActivity
import com.cabral.lucrodereceitas.R
import com.cabral.lucrodereceitas.databinding.FragmentRecipeBinding
import com.google.android.material.badge.BadgeDrawable


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private val activity by lazy { requireActivity() as LoggedActivity }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
           // findNavController().navigate(RecipeFragmentDirections.actionRecipeToTest())
            binding.test.finishLoading(false,true)
        }
        binding.test.abSetOnClickListener {
            binding.test.startLoading()
        }

    }

    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(requireContext(), color);
    }

    companion object {

        @JvmStatic
        fun newInstance() = RecipeFragment()
    }
}