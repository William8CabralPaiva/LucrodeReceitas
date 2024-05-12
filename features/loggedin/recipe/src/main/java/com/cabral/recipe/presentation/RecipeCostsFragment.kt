package com.cabral.recipe.presentation

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cabral.arch.extensions.removeEndZero
import com.cabral.core.common.domain.model.IngredientCosts
import com.cabral.model.toRecipe
import com.cabral.recipe.R
import com.cabral.recipe.databinding.RecipeCostsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class RecipeCostsFragment : Fragment() {

    private var _binding: RecipeCostsFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeCostsFragmentArgs by navArgs()

    private val viewModel: RecipeCostsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeCostsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initObservers()
    }

    private fun initObservers() {
        viewModel.run {

            notifyStartLoading.observe(viewLifecycleOwner){
                binding.viewFlipper.displayedChild = 0
            }

            notifyStopLoading.observe(viewLifecycleOwner){
                binding.viewFlipper.displayedChild = 1
            }

            notifySuccess.observe(viewLifecycleOwner) {
                it.forEach {
                    val textView =  createTextView()
                    textView.text = it.toText()

                    binding.llCosts.addView(textView)
                }
            }
        }
    }

    private fun createTextView():TextView{
       return  TextView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ContextCompat.getColor(context, com.cabral.design.R.color.design_black));
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            typeface = ResourcesCompat.getFont(
                context,
                DesignR.font.design_dancing_script_bold
            )
        }
    }

    private fun IngredientCosts.toText(): String {
        return String.format(
            getString(com.cabral.design.R.string.design_ingredient_costs_details),
            volume,
            name,
            total
        )
    }

    private fun initArgs() {
        args.currentRecipe?.run {
            viewModel.recipe = toRecipe()
            viewModel.teste()
        }

    }


}