package com.cabral.listingredients.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.listingredients.presentation.adapter.ListIngredientsViewHolder.Companion.typeViewHolder

class ListIngredientAdapter(private val context: Context) :
    ListAdapter<Ingredient, ListIngredientsViewHolder>(DIFF_CALLBACK) {

    lateinit var onClick: (Ingredient) -> Unit
    lateinit var onClickTrash: (Ingredient) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListIngredientsViewHolder {
        return typeViewHolder(parent, context, viewType)
    }

    override fun onBindViewHolder(holder: ListIngredientsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClick, onClickTrash)
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