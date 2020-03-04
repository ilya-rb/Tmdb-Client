package com.illiarb.tmdbclient.home.delegates.nowplaying

import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.LoopingPagerAdapter
import com.illiarb.tmdblcient.core.domain.Movie

class NowPlayingPagerAdapter(clickListener: OnClickListener<Movie>) : ListDelegationAdapter<List<Movie>>(),
  LoopingPagerAdapter {

  override val realCount: Int
    get() = items.size

  init {
    delegatesManager.addDelegate(nowPlayingItemDelegate(clickListener))
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    super.onBindViewHolder(holder, getRealPosition(position))
  }

  override fun getItemViewType(position: Int): Int {
    return super.getItemViewType(getRealPosition(position))
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any?>) {
    super.onBindViewHolder(holder, getRealPosition(position), payloads)
  }

  override fun getItemCount(): Int = if (realCount <= 1) realCount else LoopingPagerAdapter.MAX_COUNT
}