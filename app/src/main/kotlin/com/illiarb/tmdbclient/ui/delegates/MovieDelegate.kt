package com.illiarb.tmdbclient.ui.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
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
) = adapterDelegate<Movie, Any>(R.layout.item_movie) {

  val image = itemView.findViewById<ImageView>(R.id.itemMoviePoster)
  val title = itemView.findViewById<TextView>(R.id.itemMovieTitle)
  val card = itemView.findViewById<View>(R.id.itemMovieCard)
  val rating = itemView.findViewById<TextView>(R.id.itemMovieRating)
  val imageCornerRadius = itemView.dimen(R.dimen.corner_radius_normal)

  itemView.setSize(widthSpec = widthSpec)

  card.setSize(heightSpec = heightSpec)

  bind {
    title.text = item.title

    rating.setInvisible(item.voteAverage == 0f)
    rating.text = item.voteAverage.toString()

    image.clear()
    image.loadImage(item.posterPath) {
      cornerRadius(imageCornerRadius)
      crop(CropOptions.CenterCrop)
    }

    itemView.setOnClickListener {
      clickListener(item)
    }
  }
}