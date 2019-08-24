package com.illiarb.tmdbclient.movies.home.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.domain.entity.SearchResult
import kotlinx.android.synthetic.main.item_search_result.view.*
import javax.inject.Inject

class SearchResultsAdapter @Inject constructor(
    imageLoader: ImageLoader
) : ListDelegationAdapter<List<MovieSection>>() {

    init {
        delegatesManager.addDelegate(searchResultDelegate(imageLoader))
    }
}

fun searchResultDelegate(imageLoader: ImageLoader) =
    adapterDelegate<SearchResult, MovieSection>(R.layout.item_search_result) {

        bind {
            itemView.itemSearchTitle.text = item.title

            item.movie.posterPath?.let {
                imageLoader.fromUrl(it, itemView.itemSearchImage, RequestOptions.create {
                    cropOptions(CropOptions.CENTER_CROP)
                })
            }
        }
    }