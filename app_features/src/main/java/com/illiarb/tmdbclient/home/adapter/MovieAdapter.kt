package com.illiarb.tmdbclient.home.adapter

import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.MovieSection
import kotlinx.android.synthetic.main.item_genres_section.view.*
import javax.inject.Inject

class MovieAdapter @Inject constructor(
    imageLoader: ImageLoader,
    clickListener: OnClickListener
) : ListDelegationAdapter<List<MovieSection>>() {

    init {
        delegatesManager.addDelegate(movieSectionDelegate(imageLoader, clickListener))
        delegatesManager.addDelegate(nowPlayingDelegate(imageLoader, clickListener))
        delegatesManager.addDelegate(genresSectionDelegate())
    }

    private fun genresSectionDelegate() =
        adapterDelegate<GenresSection, MovieSection>(R.layout.item_genres_section) {

            val chipGroup = itemView.genresChipGroup

            bind {
                item.genres.forEach { genre ->
                    chipGroup.addView(
                        Chip(itemView.context).apply { text = genre.name }
                    )
                }
            }
        }
}