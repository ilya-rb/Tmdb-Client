package com.illiarb.tmdbclient.home.delegates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.RecyclerViewStateSaver
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SimpleStateSaver
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.domain.ListSection
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection

@Suppress("LongMethod")
fun movieSectionDelegate(
    clickListener: OnClickListener,
    recyclerViewStateSaver: RecyclerViewStateSaver
) = adapterDelegate<ListSection, MovieSection>(R.layout.item_movie_section) {

    val adapter = MovieSectionAdapter(clickListener)
    val sectionTitle = itemView.findViewById<TextView>(R.id.itemSectionTitle)
    val sectionList = itemView.findViewById<RecyclerView>(R.id.itemMovieSectionList)
    val seeAllButton = itemView.findViewById<View>(R.id.itemSectionSeeAll)

    val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    val stateCallback = SimpleStateSaver(sectionList)

    sectionList.let {
        it.adapter = adapter
        it.layoutManager = layoutManager
        it.setHasFixedSize(true)
        it.addItemDecoration(
            SpaceDecoration.edgeInnerSpace(
                it.dimen(R.dimen.spacing_normal),
                it.dimen(R.dimen.spacing_small)
            )
        )
    }

    bind {
        sectionTitle.text = item.title

        adapter.items = item.movies
        adapter.notifyDataSetChanged()

        seeAllButton.setOnClickListener {
            clickListener.onClick(item.code)
        }

        recyclerViewStateSaver.registerAndRestoreIfNeeded(stateCallback, item.hashCode().toString(), sectionList)
    }

    onViewDetachedFromWindow {
        recyclerViewStateSaver.unregisterCallback(stateCallback)
    }
}

private class MovieSectionAdapter(clickListener: OnClickListener) : ListDelegationAdapter<List<Movie>>() {

    init {
        delegatesManager.addDelegate(
            movieDelegate(
                clickListener,
                SizeSpec.Fixed(R.dimen.item_movie_width),
                SizeSpec.Fixed(R.dimen.item_movie_height)
            )
        )
    }
}