package com.illiarb.tmdbclient.modules.details.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMovieInfoBinding
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieDetailsSection
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieDetailsSection.MovieInfo

fun movieInfoDelegate() =
  adapterDelegateViewBinding<MovieInfo, MovieDetailsSection, ItemMovieInfoBinding>(
    { inflater, root -> ItemMovieInfoBinding.inflate(inflater, root, false) }
  ) {

    bind {
      binding.movieDetailsTitle.text = item.movie.title
      binding.movieDetailsOverview.text = item.movie.overview
      binding.movieDetailsCountry.text = item.movie.country
      binding.movieDetailsDate.text = item.movie.releaseDate.date
      binding.movieDetailsTags.text = item.movie.getGenresString()
      binding.movieDetailsLength.text =
        getString(R.string.movie_details_duration, item.movie.runtime)
    }
  }