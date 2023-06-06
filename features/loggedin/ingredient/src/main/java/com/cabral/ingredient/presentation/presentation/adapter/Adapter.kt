package com.cabral.ingredient.presentation.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabral.core.common.domain.model.Ingredients
import com.cabral.ingredient.presentation.presentation.adapter.IngredientViewHolder.Companion.typeViewHolder

class Adapter(private val context: Context) :
    ListAdapter<Ingredients, IngredientViewHolder>(DIFF_CALLBACK) {

    lateinit var onClickTrash: (Ingredients) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return typeViewHolder(parent, context, viewType)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClickTrash)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).id
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Ingredients>() {
                override fun areItemsTheSame(
                    oldItem: Ingredients,
                    newItem: Ingredients
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: Ingredients,
                    newItem: Ingredients
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}