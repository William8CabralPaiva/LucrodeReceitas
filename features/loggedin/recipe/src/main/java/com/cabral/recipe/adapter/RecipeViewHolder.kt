package com.cabral.recipe.adapter

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cabral.arch.extensions.roundingNumber
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.recipe.databinding.RecipeItemListBinding
import com.cabral.design.R as DesignR


class RecipeViewHolder(
    private val binding: RecipeItemListBinding,
    private val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        recipe: RecipeProfitPrice,
        onClick: (RecipeProfitPrice) -> Unit,
        onClickEdit: (RecipeProfitPrice) -> Unit,
        onClickTrash: (RecipeProfitPrice) -> Unit
    ) {
        binding.apply {
            recipeName.text = recipe.name

            setPrices(recipe)

            container.setOnClickListener { onClick(recipe) }
            icListBook.setOnClickListener { onClickEdit(recipe) }
            if (recipePrice.isVisible) {
                textView.setOnClickListener { onClickEdit(recipe) }
            }
            recipePrice.setOnClickListener { onClickEdit(recipe) }
            trash.setOnClickListener { onClickTrash(recipe) }
        }
    }

    private fun setPrices(recipe: RecipeProfitPrice) {
        val (price, textInfo, color) = getPriceInfo(recipe)

        setTextViewContent(textInfo, color)
        setRecipePrice(price, color)
    }

    private fun getPriceInfo(recipe: RecipeProfitPrice): Triple<Float?, String, Int> {
        return when {
            recipe.ingredientList.isNotEmpty() -> calculatePriceFromIngredients(recipe)
            else -> calculatePriceFromNoIngredients()
        }
    }

    private fun calculatePriceFromIngredients(recipe: RecipeProfitPrice): Triple<Float?, String, Int> {
        var price: Float? = null
        var textInfo = ""
        var color = 0

        when {
            recipe.profitPrice != null && recipe.profitPrice != 0f && recipe.expectedProfit != null -> {
                color = context.getColor(DesignR.color.design_orange)
                price = recipe.profitPriceUnit ?: recipe.profitPrice
                textInfo = if (recipe.profitPriceUnit != null) {
                    context.getString(DesignR.string.design_suggestion_by_unit)
                } else {
                    context.getString(DesignR.string.design_suggestion_total)
                }
            }
            recipe.volume == null && recipe.costsPerUnit == null -> {
                recipe.costs?.let {
                    color = context.getColor(DesignR.color.design_dark_red)
                    textInfo = context.getString(DesignR.string.design_cost_total)
                    price = it
                }
            }
            recipe.costs != 0f -> {
                recipe.costsPerUnit?.let {
                    color = context.getColor(DesignR.color.design_dark_red)
                    textInfo = context.getString(DesignR.string.design_cost_unit)
                    price = it
                }
            }
        }

        return Triple(price, textInfo, color)
    }

    private fun calculatePriceFromNoIngredients(): Triple<Float?, String, Int> {
        val textInfo = context.getString(DesignR.string.design_no_ingredients_register)
        val color = context.getColor(DesignR.color.design_gray_dark)
        return Triple(null, textInfo, color)
    }

    private fun setTextViewContent(textInfo: String, color: Int) {
        binding.textView.run {
            val content = SpannableString(textInfo).apply {
                setSpan(UnderlineSpan(), 0, length, 0)
            }
            text = content
            setTextColor(color)
            visibility = View.VISIBLE
        }
    }

    private fun setRecipePrice(price: Float?, color: Int) {
        if (price != null) {
            val content = SpannableString(
                String.format(
                    context.getString(com.cabral.design.R.string.design_value_format),
                    price.roundingNumber()
                )
            ).apply {
                setSpan(UnderlineSpan(), 0, length, 0)
            }
            binding.recipePrice.run {
                text = content
                setTextColor(color)
                visibility = View.VISIBLE
            }
            binding.icListBook.visibility = View.VISIBLE
        } else {
            binding.icListBook.visibility = View.GONE
        }
    }


    companion object {
        fun typeViewHolder(
            parent: ViewGroup,
            context: Context,
        ): RecipeViewHolder {
            val binding = RecipeItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return RecipeViewHolder(binding, context)

        }
    }
}