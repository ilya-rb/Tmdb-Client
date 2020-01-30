package com.illiarb.tmdbclient.home.delegates

import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdblcient.core.domain.Movie

fun nowPlayingItemDelegate(clickListener: OnClickListener) =
    adapterDelegate<Movie, Movie>(R.layout.item_now_playing_item) {

        val title = itemView.findViewById<TextView>(R.id.itemNowPlayingTitle)
        val poster = itemView.findViewById<ImageView>(R.id.itemNowPlayingPoster)
        val rating = itemView.findViewById<TextView>(R.id.itemNowPlayingRating)

        bind {
            title.text = item.title

            rating.setVisible(item.voteAverage > 0)
            rating.text = item.voteAverage.toString()

            poster.loadImage(item.backdropPath)

            itemView.setOnClickListener {
                clickListener(item)
            }
        }
    }