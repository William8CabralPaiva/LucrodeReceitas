package com.cabral.listrecipe.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.core.common.domain.model.Recipe
import com.cabral.listrecipe.databinding.ItemRecipeBinding

class RecipeViewHolder(private val binding: ItemRecipeBinding, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        recipe: Recipe,
        onClick: (Recipe) -> Unit,
        onClickTrash: (Recipe) -> Unit
    ) {
        binding.apply {
            recipeName.text = recipe.name
            recipePrice.text = String.format(
                context.getString(com.cabral.design.R.string.design_value_format),
                recipe.price.toString()
            )
            container.setOnClickListener { onClick(recipe) }
            trash.setOnClickListener { onClickTrash(recipe) }
        }
    }

    companion object {
        fun typeViewHolder(
            parent: ViewGroup,
            context: Context,
            index: Int
        ): RecipeViewHolder {
            val binding = ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return RecipeViewHolder(binding, context)

        }
    }
}