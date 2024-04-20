package com.cabral.recipe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabral.arch.extensions.removeEndZero
import com.cabral.core.common.domain.model.Recipe
import com.cabral.recipe.databinding.RecipeItemListBinding

class RecipeViewHolder(
    private val binding: RecipeItemListBinding,
    private val context: Context
) :
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
                "valor qualquer"//recipe.price.removeEndZero()//todo alterar valor
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
            val binding = RecipeItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return RecipeViewHolder(binding, context)

        }
    }
}