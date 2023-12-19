package com.cabral.recipe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.arch.extensions.removeEndZero
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.recipe.databinding.RecipeIngredientsItemBinding

class IngredientViewHolder(
    private val binding: RecipeIngredientsItemBinding,
    private val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        ingredient: Ingredient,
        onClickTrash: (Ingredient) -> Unit,
        onClickEdit: (Ingredient) -> Unit,
    ) {
        binding.apply {
            name.text = ingredient.name
            volume.text = String.format(
                context.getString(com.cabral.design.R.string.design_ingredient_format),
                ingredient.volume.removeEndZero(),
                ingredient.unit
            )
            edit.setOnClickListener { onClickEdit(ingredient) }
            trash.setOnClickListener { onClickTrash(ingredient) }
        }
    }

    companion object {
        fun typeViewHolder(
            parent: ViewGroup,
            context: Context,
            index: Int
        ): IngredientViewHolder {
            val binding = RecipeIngredientsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return IngredientViewHolder(binding, context)

        }
    }
}