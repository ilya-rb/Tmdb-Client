package com.illiarb.tmdbclient.modules.details.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMovieSimilarBinding
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SizeSpec
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.modules.delegates.movieDelegate
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieDetailsSection
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieDetailsSection.MovieSimilar
import com.illiarb.tmdbclient.services.tmdb.api.domain.Movie
import com.illiarb.tmdbclient.libs.ui.R as UiR

fun movieSimilarDelegate(
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<MovieSimilar, MovieDetailsSection, ItemMovieSimilarBinding>(
  { inflater, root -> ItemMovieSimilarBinding.inflate(inflater, root, false) }
) {

  val moviesAdapter = DelegatesAdapter(
    movieDelegate(
      SizeSpec.Fixed(R.dimen.item_movie_width),
      SizeSpec.Fixed(R.dimen.item_movie_height),
      clickListener
    )
  )

  binding.movieDetailsSimilar.apply {
    adapter = moviesAdapter
    layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    isNestedScrollingEnabled = false
    addItemDecoration(
      SpaceDecoration.edgeInnerSpace(
        itemView.dimen(UiR.dimen.spacing_normal),
        itemView.dimen(UiR.dimen.spacing_small)
      )
    )
  }

  bind {
    moviesAdapter.submitList(item.movies)
  }
}