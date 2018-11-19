package com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ilya-rb on 04.11.18.
 */
abstract class BaseDelegateViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    abstract fun bind(item: Any)

    abstract fun onViewRecycled()

    open fun bindClickListener(clickListener: View.OnClickListener) {
    }
}