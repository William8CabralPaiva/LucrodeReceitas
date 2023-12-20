package com.cabral.ingredient.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.ingredient.presentation.adapter.IngredientViewHolder.Companion.typeViewHolder

class IngredientAdapter(private val context: Context) :
    ListAdapter<Ingredient, IngredientViewHolder>(DIFF_CALLBACK) {

    lateinit var onClickTrash: (Ingredient) -> Unit
    lateinit var onClickEdit: (Ingredient) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return typeViewHolder(parent, context, viewType)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClickTrash, onClickEdit)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).id
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Ingredient>() {
                override fun areItemsTheSame(
                    oldItem: Ingredient,
                    newItem: Ingredient
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: Ingredient,
                    newItem: Ingredient
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}