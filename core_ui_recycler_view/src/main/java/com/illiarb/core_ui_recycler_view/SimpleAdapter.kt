package com.illiarb.core_ui_recycler_view

import androidx.recyclerview.widget.RecyclerView

abstract class SimpleAdapter<I, T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    val items = mutableListOf<I>()

    override fun getItemCount(): Int = items.size
}