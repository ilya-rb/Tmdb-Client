package com.illiarb.tmdbclient.feature.movies.list.delegates.movie

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseDelegateViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdblcient.core.entity.ListSection
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.system.EventBus
import kotlinx.android.synthetic.main.item_movie_section.view.*
import javax.inject.Inject

/**
 * @author ilya-rb on 04.11.18.
 */
class MovieSectionDelegate @Inject constructor(private val eventBus: EventBus) : AdapterDelegate {

    override fun isForViewType(item: Any): Boolean = item is ListSection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_movie_section), eventBus)
    }

    class ViewHolder(
        containerView: View,
        private val eventBus: EventBus
    ) : BaseDelegateViewHolder(containerView) {

        companion object {
            const val PREFETCH_ITEM_COUNT = 4
        }

        private val sectionList = itemView.itemMovieSectionList
        private val sectionTitle = itemView.itemMovieSectionTitle

        private val itemSpacing = itemView.resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
        private val adapter = MovieAdapter()
            .apply {
                clickEvent = { _, _, item -> eventBus.postEvent(item) }
            }

        init {
            sectionList.let {
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                    .apply {
                        initialPrefetchItemCount =
                            PREFETCH_ITEM_COUNT
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
}