package com.illiarb.tmdbclient.libs.ui.widget.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class DelegatesAdapter<T>(
  vararg delegates: AdapterDelegate<List<T>>,
  itemDiff: (old: T, new: T) -> Boolean = { old, new -> old == new }
) : AsyncListDifferDelegationAdapter<T>(simpleDiffUtilCallback(itemDiff)) {

  private var sharedRecycledViewPool: RecyclerView.RecycledViewPool? = null

  init {
    delegates.forEach {
      delegatesManager.addDelegate(it)

      if (it is HasSharedRecycledViewPool) {
        if (sharedRecycledViewPool == null) {
          sharedRecycledViewPool = RecyclerView.RecycledViewPool()
        }
        it.setSharedViewPool(sharedRecycledViewPool!!)
      }
    }
  }

  fun submitList(items: List<T>) {
    differ.submitList(items)
  }
}

interface HasSharedRecycledViewPool {

  fun setSharedViewPool(viewPool: RecyclerView.RecycledViewPool)
}

internal inline fun <T> simpleDiffUtilCallback(
  crossinline itemDiff: (old: T, new: T) -> Boolean
): DiffUtil.ItemCallback<T> {
  return object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
      return if (oldItem === newItem) {
        true
      } else {
        itemDiff(oldItem, newItem)
      }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: T, newItem: T) = Any()
  }
}