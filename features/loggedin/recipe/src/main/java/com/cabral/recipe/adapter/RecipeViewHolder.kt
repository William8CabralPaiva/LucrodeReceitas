package com.cabral.recipe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.arch.extensions.roundingNumber
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.recipe.databinding.RecipeItemListBinding

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
            recipe.profitPrice?.run {
                suggestion.visibility = View.VISIBLE
                recipePrice.visibility = View.VISIBLE
                recipePrice.text = String.format(
                    context.getString(com.cabral.design.R.string.design_value_format),
                    roundingNumber()
                )
            }
            container.setOnClickListener { onClick(recipe) }
            pencil.setOnClickListener { onClickEdit(recipe) }
            trash.setOnClickListener { onClickTrash(recipe) }
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