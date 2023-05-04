package com.cabral.listrecipe.presentation.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

 class SwipeGesture(private val list: List<Any>) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        Collections.swap(list, fromPosition, toPosition)
        recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
        return false
    }

     override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
         TODO("Not yet implemented")
     }
 }