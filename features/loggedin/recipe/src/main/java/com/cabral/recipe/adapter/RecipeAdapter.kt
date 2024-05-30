package com.cabral.recipe.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.recipe.adapter.RecipeViewHolder.Companion.typeViewHolder

class RecipeAdapter(private val context: Context) :
    ListAdapter<RecipeProfitPrice, RecipeViewHolder>(DIFF_CALLBACK) {

    lateinit var onClick: (RecipeProfitPrice) -> Unit
    lateinit var onClickTrash: (RecipeProfitPrice) -> Unit
    lateinit var onClickEdit: (RecipeProfitPrice) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return typeViewHolder(parent, context, viewType)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClick, onClickEdit, onClickTrash)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).id
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<RecipeProfitPrice>() {
                override fun areItemsTheSame(
                    oldItem: RecipeProfitPrice,
                    newItem: RecipeProfitPrice
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: RecipeProfitPrice,
                    newItem: RecipeProfitPrice
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}