package com.illiarb.tmdbclient.common.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.setSize
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdblcient.core.domain.Movie

fun movieDelegate(
    clickListener: OnClickListener,
    widthSpec: SizeSpec,
    heightSpec: SizeSpec
) = adapterDelegate<Movie, Movie>(R.layout.item_movie) {

    val image = itemView.findViewById<ImageView>(R.id.itemMoviePoster)
    val title = itemView.findViewById<TextView>(R.id.itemMovieTitle)
    val card = itemView.findViewById<View>(R.id.itemMovieCard)
    val rating = itemView.findViewById<TextView>(R.id.itemMovieRating)
    val imageCornerRadius = itemView.dimen(R.dimen.corner_radius_normal)

    itemView.setSize(widthSpec = widthSpec)

    card.setSize(heightSpec = heightSpec)

    itemView.setOnClickListener {
        clickListener.onClick(item)
    }

    bind {
        title.text = item.title

        rating.setVisible(item.voteAverage != 0f)
        rating.text = item.voteAverage.toString()

        image.loadImage(item.posterPath) {
            cornerRadius(imageCornerRadius)
            crop(CropOptions.CenterCrop)
            crossFade(false)
        }
    }
}