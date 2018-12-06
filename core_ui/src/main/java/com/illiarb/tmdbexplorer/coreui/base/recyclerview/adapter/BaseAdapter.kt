package com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder

abstract class BaseAdapter<T, VH : BaseViewHolder<T>>(diffCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(diffCallback) {

    var clickEvent: (viewId: Int, position: Int, item: T) -> Unit = { _, _, _ -> }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindClickListener(View.OnClickListener { view ->
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < itemCount) {
                val item = getItem(adapterPosition)
                if (item != null) {
                    clickEvent(view.id, adapterPosition, item)
                }
            }
        })

        if (position != RecyclerView.NO_POSITION && position < itemCount) {
            val item = getItem(position)
            if (item != null) {
                holder.bind(item)
            }
        }
    }

    fun getItemAt(position: Int) : T {
        return getItem(position)
    }
}