package com.illiarb.tmdbclient.common.delegates

import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.core_ui_image.loadImage
import com.illiarb.tmdbclient.home.adapter.MovieSectionAdapter
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdblcient.core.domain.Movie

fun movieDelegate(clickListener: OnClickListener) =
    adapterDelegate<Movie, Movie>(R.layout.item_movie) {

        val image = itemView.findViewById<ImageView>(R.id.itemMoviePoster)
        val title = itemView.findViewById<TextView>(R.id.itemMovieTitle)
        val imageCornerRadius = itemView.dimen(R.dimen.corner_radius_normal)

        itemView.setOnClickListener {
            clickListener.onClick(item)
        }

        bind {
            title.text = item.title

            image.loadImage(item.posterPath, RequestOptions.requestOptions {
                cornerRadius(imageCornerRadius)
                cropOptions(CropOptions.CENTER_CROP)
                thumbnail(MovieSectionAdapter.IMAGE_THUMB_FACTOR)
            })
        }
    }