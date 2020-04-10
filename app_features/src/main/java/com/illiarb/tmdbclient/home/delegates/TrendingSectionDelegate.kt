package com.illiarb.tmdbclient.home.delegates

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.common.SimpleBundleStore
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem

private const val KEY_TRENDING_STATE = "trending_state"

@Suppress("LongMethod")
fun trendingSection(
  bundleStore: SimpleBundleStore,
  clickListener: OnClickListener<Movie>
) = adapterDelegate<TrendingSection, MovieSection>(R.layout.item_trending_section) {

  val trendingList = itemView.findViewById<RecyclerView>(R.id.itemTrendingSectionList)
  val adapter = TrendingSectionAdapter(clickListener).apply {
    stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
  }

  trendingList.let {
    it.adapter = adapter
    it.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    it.layoutManager!!.onRestoreInstanceState(bundleStore.getParcelable(KEY_TRENDING_STATE))
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
  }

  onViewDetachedFromWindow {
    bundleStore.saveInstanceState {
      putParcelable(KEY_TRENDING_STATE, trendingList.layoutManager?.onSaveInstanceState())
    }
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
        image.loadImage(item.movie.posterPath) {
          crop(CropOptions.Circle)
        }

        itemView.setOnClickListener {
          clickListener(item.movie)
        }
      }
    }
}