package com.illiarb.tmdbclient.feature.home.list.ui.delegates.movie

import android.view.View
import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.common.ViewClickListener
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutOrientation
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.AdapterDelegate
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
    private val imageLoader: ImageLoader,
    private val viewClickListener: ViewClickListener
) : AdapterDelegate {

    override fun isForViewType(item: Any): Boolean = item is ListSection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_movie_section), imageLoader, viewClickListener)
    }

    class ViewHolder(
        containerView: View,
        imageLoader: ImageLoader,
        viewClickListener: ViewClickListener
    ) : BaseDelegateViewHolder(containerView) {

        private val sectionList = itemView.itemMovieSectionList
        private val sectionTitle = itemView.itemSectionTitle

        private val itemSpacing = itemView.resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
        private val movieAdapter = MovieAdapter(imageLoader, viewClickListener)

        init {
            RecyclerViewBuilder
                .create {
                    adapter(movieAdapter)
                    type(LayoutType.Linear(LayoutType.DEFAULT_PREFETCH_COUNT))
                    orientation(LayoutOrientation.HORIZONTAL)
                    hasFixedSize(true)
                    spaceBetween { spacing = itemSpacing / 2 }
                }
                .setupWith(sectionList)
        }

        override fun bind(item: Any) {
            if (item !is MovieSection) {
                return
            }

            sectionTitle.text = item.title

            movieAdapter.submitList(item.movies)
        }

        override fun bindClickListener(clickListener: View.OnClickListener) {
            super.bindClickListener(clickListener)
            itemView.setOnClickListener {
            }
        }

        override fun onViewRecycled() {
        }
    }
}