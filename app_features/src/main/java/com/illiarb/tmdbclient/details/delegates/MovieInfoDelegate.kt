package com.illiarb.tmdbclient.details.delegates

import android.widget.TextView
import androidx.emoji.text.EmojiCompat
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.details.MovieDetailsModel.MovieInfo
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdblcient.core.util.DateFormatter

fun movieInfoDelegate(dateFormatter: DateFormatter) = adapterDelegate<MovieInfo, Any>(R.layout.item_movie_info) {

    val title = itemView.findViewById<TextView>(R.id.movieDetailsTitle)
    val overview = itemView.findViewById<TextView>(R.id.movieDetailsOverview)
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
        tags.text = item.movie.getGenresString().getOrElse { "" }
    }
}