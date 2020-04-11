package com.illiarb.tmdbclient.ui.details.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel.MoviePhotos
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.services.tmdb.domain.Image

fun photoSectionDelegate(
  clickListener: OnClickListener<Image>
) = adapterDelegate<MoviePhotos, Any>(R.layout.item_movie_photos) {

  val photosAdapter = DelegatesAdapter(photoDelegate(clickListener))
  val photosList = itemView.findViewById<RecyclerView>(R.id.movieDetailsPhotos)

  photosList.apply {
    adapter = photosAdapter
    layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    setHasFixedSize(true)
    isNestedScrollingEnabled = false
    addItemDecoration(
      SpaceDecoration.edgeInnerSpace(
        itemView.dimen(R.dimen.spacing_normal),
        itemView.dimen(R.dimen.spacing_small)
      )
    )
  }

  bind {
    photosAdapter.submitList(item.movie.images)
  }
}