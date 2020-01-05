package com.illiarb.tmdbexplorer.coreui.widget.recyclerview

import androidx.lifecycle.Observer
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener

class DelegatesAdapter<T>(
    delegates: (OnClickListener) -> List<AdapterDelegate<List<T>>>,
    onClickListener: OnClickListener = OnClickListener.DefaultOnClickListener()
) : ListDelegationAdapter<List<T>>(), OnClickListener by onClickListener, Observer<List<T>> {

    init {
        delegates(this).forEach { delegatesManager.addDelegate(it) }
    }

    override fun onChanged(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun submitList(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }
}