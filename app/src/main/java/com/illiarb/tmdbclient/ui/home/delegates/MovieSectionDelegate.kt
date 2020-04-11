package com.illiarb.tmdbclient.ui.home.delegates

import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.ui.delegates.movieDelegate
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.RecyclerViewStateSaver
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.StateSaver
import com.tmdbclient.servicetmdb.domain.ListSection
import com.tmdbclient.servicetmdb.domain.Movie
import com.tmdbclient.servicetmdb.domain.MovieSection

@Suppress("LongMethod")
fun movieSection(
  recyclerViewStateSaver: RecyclerViewStateSaver,
  seeAllClickListener: OnClickListener<String>,
  movieClickListener: OnClickListener<Movie>
) = adapterDelegate<ListSection, MovieSection>(R.layout.item_movie_section) {

  val adapter = MovieSectionAdapter(movieClickListener)
  val sectionTitle = itemView.findViewById<TextView>(R.id.itemSectionTitle)
  val sectionList = itemView.findViewById<RecyclerView>(R.id.itemMovieSectionList)
  val seeAllButton = itemView.findViewById<View>(R.id.itemSectionSeeAll)

  var key: String? = null
  val stateCallback: StateSaver = {
    key?.let {
      putParcelable(it, sectionList.layoutManager?.onSaveInstanceState())
    }
  }

  sectionList.let {
    it.adapter = adapter
    it.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
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
      seeAllClickListener(item.code)
    }

    key = item.hashCode().toString().also {
      recyclerViewStateSaver.registerStateSaver(it, stateCallback)
    }

    val state = recyclerViewStateSaver.state(key) as? Parcelable?
    sectionList.layoutManager?.onRestoreInstanceState(state)
  }

  onViewDetachedFromWindow {
    key?.let { recyclerViewStateSaver.unregisterStateSaver(it) }
    key = null
  }
}

private class MovieSectionAdapter(clickListener: OnClickListener<Movie>) : ListDelegationAdapter<List<Movie>>() {

  init {
    delegatesManager.addDelegate(
      movieDelegate(
        SizeSpec.Fixed(R.dimen.item_movie_width),
        SizeSpec.Fixed(R.dimen.item_movie_height),
        clickListener
      )
    )
  }
}