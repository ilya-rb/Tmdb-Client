package com.illiarb.tmdbclient.video

import android.view.View
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.video.VideoListViewModel.UiVideo
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.setVisible

fun videoDelegate(clickListener: OnClickListener<UiVideo>) = adapterDelegate<UiVideo, Any>(R.layout.item_video) {

  val title = itemView.findViewById<TextView>(R.id.itemVideoTitle)
  val selectedView = itemView.findViewById<View>(R.id.itemVideoSelected)

  bind {
    title.text = item.video.name
    selectedView.setVisible(item.isSelected)

    itemView.setOnClickListener {
      clickListener(item)
    }
  }
}