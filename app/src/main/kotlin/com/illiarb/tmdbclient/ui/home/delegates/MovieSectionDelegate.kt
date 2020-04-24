package com.illiarb.tmdbclient.ui.home.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMovieSectionBinding
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SimpleBundleStore
import com.illiarb.tmdbclient.libs.ui.common.SizeSpec
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.services.tmdb.domain.ListSection
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection
import com.illiarb.tmdbclient.ui.delegates.movieDelegate

@Suppress("LongMethod")
fun movieSection(
  bundleStore: SimpleBundleStore,
  seeAllClickListener: OnClickListener<String>,
  movieClickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<ListSection, MovieSection, ItemMovieSectionBinding>(
  { inflater, root -> ItemMovieSectionBinding.inflate(inflater, root, false) }
) {

  val adapter = MovieSectionAdapter(movieClickListener).apply {
    stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
  }

  var saveStateKey = SimpleBundleStore.DEFAULT_SAVE_STATE_KEY

  binding.itemMovieSectionList.let {
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
    binding.itemSectionTitle.text = item.title

    adapter.items = item.movies
    adapter.notifyDataSetChanged()

    binding.itemSectionSeeAll.setOnClickListener {
      seeAllClickListener(item.code)
    }

    saveStateKey = item.code

    binding.itemMovieSectionList.layoutManager?.onRestoreInstanceState(
      bundleStore.getParcelable(saveStateKey)
    )
  }

  onViewDetachedFromWindow {
    bundleStore.putParcelable(
      saveStateKey,
      binding.itemMovieSectionList.layoutManager?.onSaveInstanceState()
    )
  }
}

private class MovieSectionAdapter(clickListener: OnClickListener<Movie>) :
  ListDelegationAdapter<List<Any>>() {

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