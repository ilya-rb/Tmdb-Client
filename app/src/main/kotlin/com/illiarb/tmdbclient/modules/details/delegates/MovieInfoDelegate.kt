package com.illiarb.tmdbclient.modules.details.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMovieInfoBinding
import com.illiarb.tmdbclient.libs.tools.DateFormatter
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieInfo

fun movieInfoDelegate(dateFormatter: DateFormatter) =
  adapterDelegateViewBinding<MovieInfo, Any, ItemMovieInfoBinding>(
    { inflater, root -> ItemMovieInfoBinding.inflate(inflater, root, false) }
  ) {

    bind {
      binding.movieDetailsTitle.text = item.movie.title
      binding.movieDetailsOverview.text = item.movie.overview
      binding.movieDetailsCountry.text = item.movie.country
      binding.movieDetailsDate.text = dateFormatter.formatDate(item.movie.releaseDate)
      binding.movieDetailsTags.text = item.movie.getGenresString()
      binding.movieDetailsLength.text =
        getString(R.string.movie_details_duration, item.movie.runtime)
    }
  }