package com.cabral.ingredient.presentation.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.ingredient.databinding.ItemIngredientBinding

class IngredientViewHolder(private val binding: ItemIngredientBinding, private val context: Context) :
RecyclerView.ViewHolder(binding.root) {

    fun bind(
        ingredient: Ingredient,
        onClickTrash: (Ingredient) -> Unit
    ) {
        binding.apply {
            name.text = ingredient.name
           volume.text = String.format(
               context.getString(com.cabral.design.R.string.design_ingredient_format),
               ingredient.volume,
               ingredient.unit
           )
            trash.setOnClickListener { onClickTrash(ingredient) }
        }
    }

    companion object {
        fun typeViewHolder(
            parent: ViewGroup,
            context: Context,
            index: Int
        ): IngredientViewHolder {
            val binding = ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return IngredientViewHolder(binding, context)

        }
    }
}