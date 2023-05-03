package com.cabral.listrecipe.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabral.listrecipe.presentation.adapter.RecipeViewHolder.Companion.typeViewHolder
import com.cabral.listrecipe.data.Recipe

class Adapter(private val context: Context) : ListAdapter<Recipe, RecipeViewHolder>(DIFF_CALLBACK) {

    lateinit var onClick: (Recipe) -> Unit
    lateinit var onClickTrash: (Recipe) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return typeViewHolder(parent, context, viewType)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClick, onClickTrash)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).id
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Recipe>() {
                override fun areItemsTheSame(
                    oldItem: Recipe,
                    newItem: Recipe
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: Recipe,
                    newItem: Recipe
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}