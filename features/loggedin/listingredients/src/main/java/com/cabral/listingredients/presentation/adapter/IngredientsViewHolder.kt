package com.cabral.listingredients.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.arch.extensions.removeEndZero
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.listingredients.databinding.ListingredientsItemIngredientBinding
import com.cabral.design.R as DesignR

class IngredientsViewHolder(
    private val binding: ListingredientsItemIngredientBinding,
    private val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        ingredient: Ingredient,
        onClick: (Ingredient) -> Unit,
        onClickTrash: (Ingredient) -> Unit
    ) {
        binding.apply {
            ingredientName.text = ingredient.name
            ingredientMensuration.text = String.format(
                context.getString(DesignR.string.design_ingredient_format),
                ingredient.volume.removeEndZero(),
                ingredient.unit
            )
            ingredientValue.text = String.format(
                context.getString(DesignR.string.design_value_format),
                ingredient.price.removeEndZero()
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
            val binding = ListingredientsItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return IngredientsViewHolder(binding, context)

        }
    }
}