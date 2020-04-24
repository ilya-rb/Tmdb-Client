package com.illiarb.tmdbclient.ui.details.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMoviePhotosBinding
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.services.tmdb.domain.Image
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel.MoviePhotos

fun photoSectionDelegate(
  clickListener: OnClickListener<Image>
) = adapterDelegateViewBinding<MoviePhotos, Any, ItemMoviePhotosBinding>(
  { inflater, root -> ItemMoviePhotosBinding.inflate(inflater, root, false) }
) {

  val photosAdapter = DelegatesAdapter(photoDelegate(clickListener))

  binding.movieDetailsPhotos.apply {
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