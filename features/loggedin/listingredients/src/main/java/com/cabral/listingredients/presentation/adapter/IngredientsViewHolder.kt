package com.cabral.listingredients.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.core.common.domain.model.Ingredients
import com.cabral.listingredients.databinding.ItemIngredientBinding

class IngredientsViewHolder(
    private val binding: ItemIngredientBinding,
    private val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        ingredient: Ingredients,
        onClick: (Ingredients) -> Unit,
        onClickTrash: (Ingredients) -> Unit
    ) {
        binding.apply {
            ingredientName.text = ingredient.name
            ingredientMensuration.text = ingredient.unit
            ingredientValue.text = String.format(
                context.getString(com.cabral.design.R.string.value_format),
                ingredient.price.toString()
            )
            container.setOnClickListener { onClick(ingredient) }
            trash.setOnClickListener { onClickTrash(ingredient) }
        }
    }

    companion object {
        fun typeViewHolder(
            parent: ViewGroup,
            context: Context,
            index: Int
        ): IngredientsViewHolder {
            val binding = ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return IngredientsViewHolder(binding, context)

        }
    }
}