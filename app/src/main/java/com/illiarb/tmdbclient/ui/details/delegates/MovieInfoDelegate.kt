package com.illiarb.tmdbclient.ui.details.delegates

import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel.MovieInfo
import com.illiarb.tmdbclient.tools.DateFormatter

fun movieInfoDelegate(dateFormatter: DateFormatter) = adapterDelegate<MovieInfo, Any>(R.layout.item_movie_info) {

  val overview = itemView.findViewById<TextView>(R.id.movieDetailsOverview)
  val title = itemView.findViewById<TextView>(R.id.movieDetailsTitle)
  val length = itemView.findViewById<TextView>(R.id.movieDetailsLength)
  val country = itemView.findViewById<TextView>(R.id.movieDetailsCountry)
  val tags = itemView.findViewById<TextView>(R.id.movieDetailsTags)
  val date = itemView.findViewById<TextView>(R.id.movieDetailsDate)

  bind {
    title.text = item.movie.title
    overview.text = item.movie.overview
    length.text = getString(R.string.movie_details_duration, item.movie.runtime)
    country.text = item.movie.country
    date.text = dateFormatter.formatDate(item.movie.releaseDate)
    tags.text = item.movie.getGenresString()
  }
}