package com.illiarb.tmdbclient.ui.home.delegates.nowplaying

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemNowPlayingItemBinding
import com.illiarb.tmdbclient.libs.imageloader.clear
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.ext.setVisible
import com.illiarb.tmdbclient.services.tmdb.domain.Movie

fun nowPlayingItemDelegate(
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<Movie, Movie, ItemNowPlayingItemBinding>(
  { inflater, root -> ItemNowPlayingItemBinding.inflate(inflater, root, false) }
) {

  bind {
    binding.itemNowPlayingTitle.text = item.title

    binding.itemNowPlayingRating.setVisible(item.voteAverage > 0)
    binding.itemNowPlayingRating.text = item.voteAverage.toString()

    binding.itemNowPlayingPoster.clear()
    binding.itemNowPlayingPoster.loadImage(item.posterPath)

    itemView.setOnClickListener {
      clickListener(item)
    }
  }
}