package com.illiarb.tmdbclient.ui.details.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.ui.delegates.movieDelegate
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.tmdbclient.servicetmdb.domain.Movie

fun movieSimilarDelegate(
  clickListener: OnClickListener<Movie>
) = adapterDelegate<MovieDetailsViewModel.MovieSimilar, Any>(R.layout.item_movie_similar) {

  val moviesList = itemView.findViewById<RecyclerView>(R.id.movieDetailsSimilar)
  val moviesAdapter = DelegatesAdapter(
    movieDelegate(
      SizeSpec.Fixed(R.dimen.item_movie_width),
      SizeSpec.Fixed(R.dimen.item_movie_height),
      clickListener
    )
  )

  moviesList.apply {
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