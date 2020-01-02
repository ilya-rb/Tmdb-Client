package com.illiarb.tmdbclient.home.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_recycler_view.LayoutOrientation
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdblcient.core.domain.ListSection
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection

fun movieSectionDelegate(clickListener: OnClickListener) =
    adapterDelegate<ListSection, MovieSection>(R.layout.item_movie_section) {

        val adapter = MovieSectionAdapter(clickListener)
        val sectionTitle = itemView.findViewById<TextView>(R.id.itemSectionTitle)
        val sectionList = itemView.findViewById<RecyclerView>(R.id.itemMovieSectionList)
        val seeAllButton = itemView.findViewById<View>(R.id.itemSectionSeeAll)

        RecyclerViewBuilder
            .create {
                adapter(adapter)
                type(LayoutType.Linear())
                orientation(LayoutOrientation.HORIZONTAL)
                hasFixedSize(true)
                spaceBetween { spacingRight = itemView.dimen(R.dimen.spacing_normal) }
            }
            .setupWith(sectionList)

        bind {
            sectionTitle.text = item.title
            adapter.items = item.movies
            adapter.notifyDataSetChanged()
            seeAllButton.setOnClickListener { clickListener.onClick(item.code) }
        }
    }

class MovieSectionAdapter(clickListener: OnClickListener) : ListDelegationAdapter<List<Movie>>() {

    init {
        delegatesManager.addDelegate(
            movieDelegate(
                clickListener,
                SizeSpec.Fixed(R.dimen.item_movie_width),
                SizeSpec.Fixed(R.dimen.item_movie_height)
            )
        )
    }

    companion object {
        const val IMAGE_THUMB_FACTOR = 0.2f
    }
}