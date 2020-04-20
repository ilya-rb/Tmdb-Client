package com.illiarb.tmdbclient.ui.home.delegates.nowplaying

import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.imageloader.clear
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.ext.setVisible
import com.illiarb.tmdbclient.services.tmdb.domain.Movie

fun nowPlayingItemDelegate(
  clickListener: OnClickListener<Movie>
) = adapterDelegate<Movie, Movie>(R.layout.item_now_playing_item) {

  val title = itemView.findViewById<TextView>(R.id.itemNowPlayingTitle)
  val poster = itemView.findViewById<ImageView>(R.id.itemNowPlayingPoster)
  val rating = itemView.findViewById<TextView>(R.id.itemNowPlayingRating)

  bind {
    title.text = item.title

    rating.setVisible(item.voteAverage > 0)
    rating.text = item.voteAverage.toString()

    poster.clear()
    poster.loadImage(item.posterPath)

    itemView.setOnClickListener {
      clickListener(item)
    }
  }
}