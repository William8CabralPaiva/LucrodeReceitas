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
        var price: Float? = 0f
        var textInfo = ""
        var color = 0

        if (recipe.ingredientList.size > 0) {
            if (recipe.profitPrice != null && recipe.profitPrice != 0f && recipe.expectedProfit != null) {
                color = context.getColor(DesignR.color.design_orange)
                if (recipe.profitPriceUnit != null) {
                    textInfo = context.getString(DesignR.string.design_suggestion_by_unit)
                    price = recipe.profitPriceUnit
                } else {
                    textInfo = context.getString(DesignR.string.design_suggestion_total)
                    price = recipe.profitPrice
                }
            } else if (recipe.volume == null && recipe.costsPerUnit == null) {
                recipe.costs?.let {
                    color = context.getColor(DesignR.color.design_dark_red)
                    textInfo = context.getString(DesignR.string.design_cost_total)
                    price = it
                }
            } else if (recipe.costs != 0f) {
                recipe.costsPerUnit?.let {
                    color = context.getColor(DesignR.color.design_dark_red)
                    textInfo = context.getString(DesignR.string.design_cost_unit)
                    price = it
                }
            }
        } else {
            textInfo = context.getString(DesignR.string.design_no_ingredients_register)
            color = context.getColor(DesignR.color.design_gray_dark)
            price = null
        }

        binding.textView.run {
            if (price != null) {
                val content = SpannableString(textInfo)
                content.setSpan(UnderlineSpan(), 0, content.length, 0)
                text = content
            } else {
                text = textInfo
            }
            setTextColor(color)
            visibility = View.VISIBLE
        }

        price?.run {
            val content = SpannableString(
                String.format(
                    context.getString(com.cabral.design.R.string.design_value_format),
                    roundingNumber()
                )
            )
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            binding.recipePrice.run {
                text = content
                setTextColor(color)
                visibility = View.VISIBLE
                binding.icListBook.visibility = View.VISIBLE
            }
        } ?: run {
            binding.icListBook.visibility = View.GONE
        }

    }

    companion object {
        fun typeViewHolder(
            parent: ViewGroup,
            context: Context,
            index: Int
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