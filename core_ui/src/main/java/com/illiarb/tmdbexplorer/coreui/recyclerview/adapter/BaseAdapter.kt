package com.illiarb.tmdbexplorer.coreui.recyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.BaseViewHolder
import java.util.Collections

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> : RecyclerView.Adapter<VH>() {

    private val currentList = mutableListOf<T>()

    var clickEvent: (viewId: Int, position: Int, item: T) -> Unit = { _, _, _ -> }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindClickListener(View.OnClickListener { view ->
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < itemCount) {
                val item = getItemAt(adapterPosition)
                if (item != null) {
                    clickEvent(view.id, adapterPosition, item)
                }
            }
        })

        if (position != RecyclerView.NO_POSITION && position < itemCount) {
            val item = getItemAt(position)
            if (item != null) {
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = currentList.size

    fun getItemAt(position: Int): T = currentList[position]

    fun readOnlyList(): List<T> = Collections.unmodifiableList(currentList)

    fun submitList(items: List<T>) {
        currentList.clear()
        currentList.addAll(items)
        notifyDataSetChanged()
    }
}