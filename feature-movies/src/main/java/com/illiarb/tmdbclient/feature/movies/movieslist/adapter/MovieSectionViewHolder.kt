package com.illiarb.tmdbclient.feature.movies.movieslist.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseDelegateViewHolder
import com.illiarb.tmdblcient.core.entity.MovieSection
import kotlinx.android.synthetic.main.item_movie_section.view.*

/**
 * @author ilya-rb on 04.11.18.
 */
class MovieSectionViewHolder(containerView: View) : BaseDelegateViewHolder(containerView) {

    companion object {
        const val PREFETCH_ITEM_COUNT = 4
    }

    private val sectionList = itemView.itemMovieSectionList
    private val sectionTitle = itemView.itemMovieSectionTitle

    private val adapter = MovieAdapter()
    private val itemSpacing = itemView.resources.getDimensionPixelSize(R.dimen.item_movie_spacing)

    init {
        sectionList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                .apply {
                    initialPrefetchItemCount = PREFETCH_ITEM_COUNT
                }
            it.addItemDecoration(SpaceItemDecoration(itemSpacing / 2, 0, true, true))
            it.setHasFixedSize(true)
        }
    }

    override fun bind(item: Any) {
        if (item !is MovieSection) {
            return
        }
        sectionTitle.text = item.title
        adapter.submitList(item.movies)
    }

    override fun onViewRecycled() {
    }
}