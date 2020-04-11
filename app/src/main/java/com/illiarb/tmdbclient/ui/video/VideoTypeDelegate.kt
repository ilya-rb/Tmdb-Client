package com.illiarb.tmdbclient.ui.video

import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.ui.video.VideoListViewModel.UiVideoSection

fun videoTypeDelegate() = adapterDelegate<UiVideoSection, Any>(R.layout.item_video_type) {

  val itemTitle = itemView.findViewById<TextView>(R.id.itemVideoType)
  val itemCount = itemView.findViewById<TextView>(R.id.itemVideoCount)

  bind {
    itemTitle.text = item.title
    itemCount.text = item.count.toString()
  }
}