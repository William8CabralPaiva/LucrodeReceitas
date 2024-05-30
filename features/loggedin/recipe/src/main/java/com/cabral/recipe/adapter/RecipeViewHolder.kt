package com.cabral.recipe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            pencil.setOnClickListener { onClickEdit(recipe) }
            trash.setOnClickListener { onClickTrash(recipe) }
        }
    }

    private fun setPrices(recipe: RecipeProfitPrice) {
        var price: Float? = 0f
        var textInfo = ""
        var color = 0

        if (recipe.profitPrice != null && recipe.profitPrice != 0f) {
            recipe.profitPrice?.let {
                textInfo = context.getString(DesignR.string.design_suggestion)
                color = context.getColor(DesignR.color.design_orange)
                price = it
            }
        } else if (recipe.profitPrice != 0f && recipe.total != 0f) {
            recipe.total?.let {
                textInfo = context.getString(DesignR.string.design_cost)
                color = context.getColor(DesignR.color.design_dark_red)
                price = it
            }
        } else {
            textInfo = context.getString(DesignR.string.design_no_ingredients_register)
            color = context.getColor(DesignR.color.design_gray_dark)
            price = null
        }

        binding.textView.run {
            text = textInfo
            setTextColor(color)
            visibility = View.VISIBLE
        }

        price?.run {
            binding.recipePrice.run {
                text = String.format(
                    context.getString(com.cabral.design.R.string.design_value_format),
                    roundingNumber()
                )
                setTextColor(color)
                visibility = View.VISIBLE
            }
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