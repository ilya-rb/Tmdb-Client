package com.illiarb.tmdbclient.ui.home.delegates

import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.imageloader.clear
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.RecyclerViewStateSaver
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.StateSaver
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection.TrendingItem

private const val KEY_TRENDING_STATE = "trending_state"

@Suppress("LongMethod")
fun trendingSection(
  stateSaver: RecyclerViewStateSaver,
  clickListener: OnClickListener<Movie>
) = adapterDelegate<TrendingSection, MovieSection>(R.layout.item_trending_section) {

  val adapter = TrendingSectionAdapter(clickListener)
  val trendingList = itemView.findViewById<RecyclerView>(R.id.itemTrendingSectionList)
  val stateCallback: StateSaver = {
    putParcelable(KEY_TRENDING_STATE, trendingList.layoutManager?.onSaveInstanceState())
  }

  trendingList.let {
    it.adapter = adapter
    it.layoutManager = LinearLayoutManager(
      itemView.context,
      LinearLayoutManager.HORIZONTAL,
      false
    )
    it.addItemDecoration(
      SpaceDecoration.edgeInnerSpace(
        it.dimen(R.dimen.spacing_normal),
        it.dimen(R.dimen.spacing_small)
      )
    )
  }

  bind {
    adapter.items = item.items
    adapter.notifyDataSetChanged()

    val state = stateSaver.state(KEY_TRENDING_STATE) as? Parcelable?
    trendingList.layoutManager?.onRestoreInstanceState(state)
  }

  onViewAttachedToWindow {
    stateSaver.registerStateSaver(KEY_TRENDING_STATE, stateCallback)
  }

  onViewDetachedFromWindow {
    stateSaver.unregisterStateSaver(KEY_TRENDING_STATE)
  }
}

private class TrendingSectionAdapter(clickListener: OnClickListener<Movie>) :
  ListDelegationAdapter<List<TrendingItem>>() {

  init {
    delegatesManager.addDelegate(trendingDelegate(clickListener))
  }

  private fun trendingDelegate(clickListener: OnClickListener<Movie>) =
    adapterDelegate<TrendingItem, TrendingItem>(R.layout.item_trending) {

      val image = itemView.findViewById<ImageView>(R.id.itemTrendingImage)
      val name = itemView.findViewById<TextView>(R.id.itemTrendingName)

      bind {
        name.text = item.movie.title

        image.clear()
        image.loadImage(item.movie.posterPath) {
          crop(CropOptions.Circle)
        }

        itemView.setOnClickListener {
          clickListener(item.movie)
        }
      }
    }
}