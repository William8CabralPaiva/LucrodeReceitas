package com.cabral.listingredients.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabral.core.common.domain.model.Ingredients
import com.cabral.listingredients.presentation.adapter.IngredientsViewHolder.Companion.typeViewHolder

class Adapter(private val context: Context) :
    ListAdapter<Ingredients, IngredientsViewHolder>(DIFF_CALLBACK) {

    lateinit var onClick: (Ingredients) -> Unit
    lateinit var onClickTrash: (Ingredients) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return typeViewHolder(parent, context, viewType)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClick, onClickTrash)
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