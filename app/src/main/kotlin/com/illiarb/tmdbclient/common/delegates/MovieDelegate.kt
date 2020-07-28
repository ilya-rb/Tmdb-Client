package com.illiarb.tmdbclient.common.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMovieBinding
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.imageloader.clear
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SizeSpec
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.setInvisible
import com.illiarb.tmdbclient.libs.ui.ext.setSize
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.util.loadTmdbImage
import com.illiarb.tmdbclient.libs.ui.R as UiR

fun movieDelegate(
  widthSpec: SizeSpec,
  heightSpec: SizeSpec,
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<Movie, Any, ItemMovieBinding>(
  { inflater, root -> ItemMovieBinding.inflate(inflater, root, false) }
) {

  val imageCornerRadius = itemView.dimen(UiR.dimen.corner_radius_normal)

  itemView.setSize(widthSpec = widthSpec)

  binding.itemMovieCard.setSize(heightSpec = heightSpec)

  fun getFixedWidthIfPossible(): Int {
    return if (widthSpec is SizeSpec.Fixed) {
      itemView.dimen(widthSpec.sizeRes)
    } else {
      0
    }
  }

  fun getFixedHeightIfPossible(): Int {
    return if (widthSpec is SizeSpec.Fixed) {
      itemView.dimen(widthSpec.sizeRes)
    } else {
      0
    }
  }

  bind {
    binding.itemMovieTitle.text = item.title

    binding.itemMovieRating.setInvisible(item.voteAverage == 0f)
    binding.itemMovieRating.text = item.voteAverage.toString()

    binding.itemMoviePoster.clear()
    binding.itemMoviePoster.loadTmdbImage(
      image = item.posterPath,
      width = getFixedWidthIfPossible(),
      height = getFixedHeightIfPossible()
    ) {
      cornerRadius(imageCornerRadius)
      crop(CropOptions.CenterCrop)
      errorRes(R.drawable.ic_image_placeholder)
    }

    itemView.setOnClickListener {
      clickListener(item)
    }
  }
}