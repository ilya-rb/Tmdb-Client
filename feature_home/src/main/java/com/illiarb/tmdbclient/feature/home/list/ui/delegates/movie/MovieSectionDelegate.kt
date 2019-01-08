package com.illiarb.tmdbclient.feature.home.list.ui.delegates.movie

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.BaseDelegateViewHolder
import com.illiarb.tmdblcient.core.entity.ListSection
import com.illiarb.tmdblcient.core.entity.MovieSection
import kotlinx.android.synthetic.main.item_movie_section.view.*
import kotlinx.android.synthetic.main.layout_section_title.view.*
import javax.inject.Inject

/**
 * @author ilya-rb on 04.11.18.
 */
class MovieSectionDelegate @Inject constructor(
    private val imageLoader: ImageLoader
) : AdapterDelegate {

    override fun isForViewType(item: Any): Boolean = item is ListSection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_movie_section), imageLoader)
    }

    class ViewHolder(
        containerView: View,
        imageLoader: ImageLoader
    ) : BaseDelegateViewHolder(containerView) {

        companion object {
            const val PREFETCH_ITEM_COUNT = 4
        }

        private val sectionList = itemView.itemMovieSectionList
        private val sectionTitle = itemView.itemSectionTitle

        private val itemSpacing = itemView.resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
        private val adapter = MovieAdapter(imageLoader)
            .apply {
                clickEvent = { _, _, item ->
                    //
                }
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