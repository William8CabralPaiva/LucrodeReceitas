package com.cabral.ingredient.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.arch.extensions.removeEndZero
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.ingredient.databinding.IngredientsItemListBinding
import com.cabral.design.R as DesignR

class ListIngredientsViewHolder(
    private val binding: IngredientsItemListBinding,
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
        ): ListIngredientsViewHolder {
            val binding = IngredientsItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ListIngredientsViewHolder(binding, context)

        }
    }
}