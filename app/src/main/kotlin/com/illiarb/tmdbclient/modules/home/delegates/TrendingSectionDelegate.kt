package com.illiarb.tmdbclient.modules.home.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemTrendingBinding
import com.illiarb.tmdbclient.databinding.ItemTrendingSectionBinding
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.imageloader.clear
import com.illiarb.tmdbclient.libs.ui.R
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SimpleBundleStore
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection.TrendingItem
import com.illiarb.tmdbclient.util.loadTmdbImage

private const val KEY_TRENDING_STATE = "trending_state"

@Suppress("LongMethod")
fun trendingSection(
  bundleStore: SimpleBundleStore,
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<TrendingSection, MovieSection, ItemTrendingSectionBinding>(
  { inflater, root -> ItemTrendingSectionBinding.inflate(inflater, root, false) }
) {

  val adapter = TrendingSectionAdapter(clickListener).apply {
    stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
  }

  binding.itemTrendingSectionList.let {
    it.adapter = adapter
    it.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
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

    binding.itemTrendingSectionList.layoutManager?.onRestoreInstanceState(
      bundleStore.getParcelable(KEY_TRENDING_STATE)
    )
  }

  onViewDetachedFromWindow {
    bundleStore.putParcelable(
      KEY_TRENDING_STATE,
      binding.itemTrendingSectionList.layoutManager?.onSaveInstanceState()
    )
  }
}

private class TrendingSectionAdapter(clickListener: OnClickListener<Movie>) :
  ListDelegationAdapter<List<TrendingItem>>() {

  init {
    delegatesManager.addDelegate(trendingDelegate(clickListener))
  }

  private fun trendingDelegate(clickListener: OnClickListener<Movie>) =
    adapterDelegateViewBinding<TrendingItem, TrendingItem, ItemTrendingBinding>(
      { inflater, root -> ItemTrendingBinding.inflate(inflater, root, false) }
    ) {

      bind {
        binding.itemTrendingName.text = item.movie.title

        binding.itemTrendingImage.clear()
        binding.itemTrendingImage.loadTmdbImage(item.movie.posterPath) {
          crop(CropOptions.Circle)
        }

        itemView.setOnClickListener {
          clickListener(item.movie)
        }
      }
    }
}