package com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(containerView: View) : RecyclerView.ViewHolder(containerView) {

    abstract fun bind(item: T)

    abstract fun onViewRecycled()

    open fun bindClickListener(clickListener: View.OnClickListener) {
    }
}