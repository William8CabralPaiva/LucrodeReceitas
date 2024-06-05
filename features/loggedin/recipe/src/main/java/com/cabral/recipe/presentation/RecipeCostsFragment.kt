package com.cabral.recipe.presentation

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.cabral.arch.BaseFragment
import com.cabral.arch.extensions.removeEndZero
import com.cabral.arch.extensions.roundingNumber
import com.cabral.core.common.domain.model.IngredientCosts
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.model.toRecipe
import com.cabral.recipe.databinding.RecipeCostsFragmentBinding
import com.google.android.gms.ads.AdRequest
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cabral.design.R as DesignR

class RecipeCostsFragment :
    BaseFragment<RecipeCostsFragmentBinding>(RecipeCostsFragmentBinding::inflate) {

    private val args: RecipeCostsFragmentArgs by navArgs()

    private val viewModel: RecipeCostsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initAds()
        initObservers()
    }

    private fun initObservers() {
        viewModel.run {

            notifyStartLoading.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 0
            }

            notifyStopLoading.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 2
            }

            notifyError.observe(viewLifecycleOwner) {
                binding.viewFlipper.displayedChild = 1
            }

            notifyTitle.observe(viewLifecycleOwner) {
                binding.title.text = it
            }


            notifyTotalPerUnit.observe(viewLifecycleOwner) {
                binding.titleSuggestion.isVisible = true
                binding.profitPriceUnit.run {
                    isVisible = true
                    text = String.format(
                        getString(DesignR.string.design_profit_price),
                        it.roundingNumber()
                    )
                }
            }

            notifyCostsPerUnit.observe(viewLifecycleOwner) {
                binding.titleCosts.isVisible = true
                binding.costsUnit.run {
                    isVisible = true
                    text = String.format(
                        getString(DesignR.string.design_cost_unit_param),
                        it.roundingNumber()
                    )
                }
            }

            notifyCosts.observe(viewLifecycleOwner) {
                binding.titleCosts.isVisible = true
                binding.costsTotal.run {
                    isVisible = true
                    text = String.format(
                        getString(DesignR.string.design_cost_total_param),
                        it.roundingNumber()
                    )
                }
            }


            notifyProfitPerUnit.observe(viewLifecycleOwner) {
                binding.titleProfit.isVisible = true
                binding.profitUnit.run {
                    isVisible = true
                    text = String.format(
                        getString(DesignR.string.design_profit_unit),
                        it.roundingNumber()
                    )
                }
            }


            notifyProfit.observe(viewLifecycleOwner) {
                binding.titleProfit.isVisible = true
                binding.profitTotal.run {
                    isVisible = true
                    text = String.format(
                        getString(DesignR.string.design_profit_total),
                        it.roundingNumber()
                    )
                }
            }

            notifyProfit.observe(viewLifecycleOwner) {
                binding.titleProfit.isVisible = true
                binding.profitTotal.run {
                    isVisible = true
                    text = String.format(
                        getString(DesignR.string.design_profit_total),
                        it.roundingNumber()
                    )
                }
            }

//            notifyProfit.observe(viewLifecycleOwner){
//                binding.profitPrice.run {
//                    isVisible = true
//                    text = String.format(
//                        getString(DesignR.string.design_total),
//                        it.roundingNumber()
//                    )
//                }
//            }


            notifyIngredients.observe(viewLifecycleOwner) {
                it.forEach {
                    val textView = createTextView()
                    textView.text = it.toText()

                    binding.llCosts.addView(textView)
                }
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
            setTextColor(context.getColor(com.cabral.design.R.color.design_black))
            setTextAppearance(DesignR.style.DesignHandWriting)
            typeface = ResourcesCompat.getFont(
                context,
                DesignR.font.design_dancing_script_bold
            )

        }
    }

    private fun IngredientCosts.toText(): String {
        if (this.unit == UnitMeasureType.U.unit) {
            return String.format(
                getString(DesignR.string.design_ingredient_costs_details_unit),
                volume.removeEndZero(),
                name,
                priceUnit.roundingNumber(),
                total.roundingNumber()
            )
        } else {
            return String.format(
                getString(DesignR.string.design_ingredient_costs_details),
                volume.removeEndZero(),
                unit,
                name,
                priceUnit.roundingNumber(),
                total.roundingNumber()
            )
        }
    }


    private fun initArgs() {
        args.currentRecipe?.run {
            viewModel.recipe = toRecipe()
            viewModel.load()
        }
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }


}