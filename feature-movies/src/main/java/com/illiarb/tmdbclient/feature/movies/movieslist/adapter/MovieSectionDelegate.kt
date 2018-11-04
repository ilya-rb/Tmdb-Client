package com.illiarb.tmdbclient.feature.movies.movieslist.adapter

import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseDelegateViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdblcient.core.entity.MovieSection
import javax.inject.Inject

/**
 * @author ilya-rb on 04.11.18.
 */
class MovieSectionDelegate @Inject constructor() : AdapterDelegate {

    override fun isForViewType(item: Any): Boolean = item is MovieSection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder {
        return MovieSectionViewHolder(parent.inflate(R.layout.item_movie_section))
    }
}