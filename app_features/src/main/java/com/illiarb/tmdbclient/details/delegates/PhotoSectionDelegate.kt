package com.illiarb.tmdbclient.details.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.details.MovieDetailsModel.MoviePhotos
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration

fun photoSectionDelegate(
    clickListener: OnClickListener
) = adapterDelegate<MoviePhotos, Any>(R.layout.item_movie_photos) {

    val photosAdapter = DelegatesAdapter(photoDelegate(clickListener))
    val photosList = itemView.findViewById<RecyclerView>(R.id.movieDetailsPhotos)

    photosList.apply {
        adapter = photosAdapter
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        setHasFixedSize(true)
        isNestedScrollingEnabled = false
        addItemDecoration(SpaceDecoration.edgeInnerSpace(0, itemView.dimen(R.dimen.spacing_small)))
    }

    bind {
        photosAdapter.submitList(item.movie.images)
    }
}