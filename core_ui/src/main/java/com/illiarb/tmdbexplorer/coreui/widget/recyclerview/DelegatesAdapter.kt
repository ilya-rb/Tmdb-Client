package com.illiarb.tmdbexplorer.coreui.widget.recyclerview

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener

class DelegatesAdapter<T>(
    delegates: (OnClickListener) -> List<AdapterDelegate<List<T>>>,
    itemDiff: (old: T, new: T) -> Boolean,
    onClickListener: OnClickListener = OnClickListener.DefaultOnClickListener()
) : AsyncListDifferDelegationAdapter<T>(
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            if (oldItem === newItem) {
                return true
            }
            return itemDiff(oldItem, newItem)
        }

        override fun getChangePayload(oldItem: T, newItem: T) = Any()

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
), OnClickListener by onClickListener, Observer<List<T>> {

    init {
        delegates(this).forEach {
            delegatesManager.addDelegate(it)
        }
    }

    override fun onChanged(items: List<T>) {
        differ.submitList(items)
    }

    fun submitList(items: List<T>) {
        differ.submitList(items)
    }
}