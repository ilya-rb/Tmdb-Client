package com.illiarb.tmdbclient.video

import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.video.VideoListModel.UiVideoSection

fun videoTypeDelegate() = adapterDelegate<UiVideoSection, Any>(R.layout.item_video_type) {

    val itemTitle = itemView.findViewById<TextView>(R.id.itemVideoType)
    val itemCount = itemView.findViewById<TextView>(R.id.itemVideoCount)

    bind {
        itemTitle.text = item.title
        itemCount.text = item.count.toString()
    }
}