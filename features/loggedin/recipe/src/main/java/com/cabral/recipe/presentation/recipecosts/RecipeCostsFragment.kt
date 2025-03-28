package com.cabral.recipe.presentation.recipecosts

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.cabral.arch.BaseFragment
import com.cabral.arch.extensions.removeEndZero
import com.cabral.arch.extensions.roundingNumber
import com.cabral.core.common.domain.model.IngredientCosts
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.model.toRecipe
import com.cabral.recipe.databinding.RecipeCostsFragmentBinding
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class RecipeCostsFragment :
    BaseFragment<RecipeCostsFragmentBinding>(RecipeCostsFragmentBinding::inflate) {

    private val args:
            RecipeCostsFragmentArgs by navArgs()
    private val viewModel: RecipeCostsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initAds()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.StartLoading -> binding.viewFlipper.displayedChild = 0
                    is UiState.StopLoading -> binding.viewFlipper.displayedChild = 2
                    is UiState.Error -> binding.viewFlipper.displayedChild = 1
                    is UiState.Success -> updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: UiState.Success) {
        binding.title.setTitle(state.title)

        state.costs.totalText(
            binding.titleCosts,
            binding.costsTotal,
            String.format(
                getString(DesignR.string.design_cost_total_param),
                state.costs.roundingNumber()
            )
        )

        state.costsPerUnit.totalText(
            binding.titleCosts,
            binding.costsUnit,
            String.format(
                getString(DesignR.string.design_cost_unit_param),
                state.costsPerUnit.roundingNumber()
            )
        )

        state.profit.totalText(
            binding.titleProfit,
            binding.profitTotal,
            String.format(
                getString(DesignR.string.design_profit_total),
                state.profit.roundingNumber()
            )
        )

        state.profitPerUnit.totalText(
            binding.titleProfit,
            binding.profitUnit,
            String.format(
                getString(DesignR.string.design_profit_unit),
                state.profitPerUnit.roundingNumber()
            )
        )

        state.totalPerUnit.totalText(
            binding.titleSuggestion,
            binding.profitPriceUnit,
            String.format(
                getString(DesignR.string.design_profit_price),
                state.totalPerUnit.roundingNumber()
            )
        )

        binding.llCosts.removeAllViews()
        state.ingredients.forEach {
            val textView = createTextView().apply { text = it.toText() }
            binding.llCosts.addView(textView)
        }
    }

    private fun Float?.totalText(title: TextView, description: TextView, textValue: String) {
        if (this != null && this > 0) {
            title.isVisible = true
            description.apply {
                isVisible = true
                text = textValue
            }
        }
    }

    private fun createTextView(): TextView {
        return TextView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(context.getColor(DesignR.color.design_black))
            setTextAppearance(DesignR.style.DesignHandWriting)
            typeface = ResourcesCompat.getFont(
                context,
                DesignR.font.design_dancing_script_bold
            )
        }
    }

    private fun IngredientCosts.toText(): String {
        return if (this.unit == UnitMeasureType.U.unit) {
            String.format(
                getString(DesignR.string.design_ingredient_costs_details_unit),
                volume.removeEndZero(),
                name,
                priceUnit.roundingNumber(),
                total.roundingNumber()
            )
        } else {
            String.format(
                getString(DesignR.string.design_ingredient_costs_details),
                volume.removeEndZero(),
                unit,
                name,
                total.roundingNumber()
            )
        }
    }

    private fun initArgs() {
        args.currentRecipe?.let {
            viewModel.recipe = it.toRecipe()
            viewModel.load()
        }
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }
}
