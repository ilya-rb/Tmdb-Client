package com.illiarb.tmdbclient.ui.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.libs.ui.R
import com.illiarb.tmdbclient.databinding.ItemMovieBinding
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.imageloader.clear
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SizeSpec
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.setInvisible
import com.illiarb.tmdbclient.libs.ui.ext.setSize
import com.illiarb.tmdbclient.services.tmdb.domain.Movie

fun movieDelegate(
  widthSpec: SizeSpec,
  heightSpec: SizeSpec,
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<Movie, Any, ItemMovieBinding>(
  { inflater, root -> ItemMovieBinding.inflate(inflater, root, false) }
) {

  val imageCornerRadius = itemView.dimen(R.dimen.corner_radius_normal)

  itemView.setSize(widthSpec = widthSpec)

  binding.itemMovieCard.setSize(heightSpec = heightSpec)

  bind {
    binding.itemMovieTitle.text = item.title

    binding.itemMovieRating.setInvisible(item.voteAverage == 0f)
    binding.itemMovieRating.text = item.voteAverage.toString()

    binding.itemMoviePoster.clear()
    binding.itemMoviePoster.loadImage(item.posterPath) {
      cornerRadius(imageCornerRadius)
      crop(CropOptions.CenterCrop)
    }

    itemView.setOnClickListener {
      clickListener(item)
    }
  }
}