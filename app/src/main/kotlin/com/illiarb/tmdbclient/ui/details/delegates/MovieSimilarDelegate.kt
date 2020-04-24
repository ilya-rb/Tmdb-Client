package com.illiarb.tmdbclient.ui.details.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMovieSimilarBinding
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SizeSpec
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.ui.delegates.movieDelegate
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel

fun movieSimilarDelegate(
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<MovieDetailsViewModel.MovieSimilar, Any, ItemMovieSimilarBinding>(
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
        itemView.dimen(R.dimen.spacing_normal),
        itemView.dimen(R.dimen.spacing_small)
      )
    )
  }

  bind {
    moviesAdapter.submitList(item.movies)
  }
}