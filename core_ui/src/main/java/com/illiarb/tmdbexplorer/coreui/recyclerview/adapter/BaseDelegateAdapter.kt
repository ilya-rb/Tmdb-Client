package com.illiarb.tmdbexplorer.coreui.recyclerview.adapter

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbexplorer.coreui.R
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.BaseDelegateViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import javax.inject.Inject

/**
 * @author ilya-rb on 04.11.18.
 */
class DelegateAdapter @Inject constructor() : RecyclerView.Adapter<BaseDelegateViewHolder>() {

    private val delegatesMap = SparseArrayCompat<AdapterDelegate>()

    private val currentList: MutableList<Any>
        get() = asyncListDiffer.currentList

    private val diffCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
    }

    private val asyncListDiffer = AsyncListDiffer<Any>(this, diffCallback)

    private var fallbackDelegate: AdapterDelegate =
        DefaultFallbackDelegate()

    private var clickEvent: (viewId: Int, position: Int, item: Any) -> Unit = { _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder {
        val delegate = delegatesMap[viewType] ?: fallbackDelegate
        return delegate.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseDelegateViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION && position < itemCount) {
            holder.bind(currentList[position])
            holder.bindClickListener(View.OnClickListener { view ->
                val adapterPosition = holder.adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < itemCount) {
                    clickEvent(view.id, adapterPosition, currentList[adapterPosition])
                }
            })
        }
    }

    override fun onViewRecycled(holder: BaseDelegateViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun getItemViewType(position: Int): Int {
        if (currentList.isEmpty()
            || position == RecyclerView.NO_POSITION
            || position > currentList.size - 1
        ) {
            return super.getItemViewType(position)
        }

        val size = delegatesMap.size()
        for (i in 0..size) {
            if (delegatesMap.get(i)?.isForViewType(currentList[position]) == true) {
                return delegatesMap.keyAt(i)
            }
        }

        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun addDelegatesFromSet(delegates: Set<AdapterDelegate>) {
        delegates.forEach { addDelegate(it) }
    }

    fun addDelegate(delegate: AdapterDelegate) {
        val viewType = delegatesMap.size()

        if (delegatesMap[viewType] == null) {
            delegatesMap.put(viewType, delegate)
        }
    }

    fun setFallbackDelegate(delegate: AdapterDelegate) {
        fallbackDelegate = delegate
    }

    fun setClickEvent(onClick: (Int, Int, Any) -> Unit) {
        clickEvent = onClick
    }

    fun submitList(newList: List<Any>) {
        asyncListDiffer.submitList(newList)
    }

    class DefaultFallbackDelegate : AdapterDelegate {
        override fun isForViewType(item: Any): Boolean = false
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder =
            object : BaseDelegateViewHolder(parent.inflate(R.layout.item_fallback)) {
                override fun bind(item: Any) {
                }

                override fun onViewRecycled() {
                }
            }
    }
}