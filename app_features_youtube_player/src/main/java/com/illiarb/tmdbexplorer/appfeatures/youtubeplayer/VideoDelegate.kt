package com.illiarb.tmdbexplorer.appfeatures.youtubeplayer

import android.view.View
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.YoutubePlayerModel.UiVideo
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.setVisible

fun videoDelegate(clickListener: OnClickListener) = adapterDelegate<UiVideo, UiVideo>(R.layout.item_video) {

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