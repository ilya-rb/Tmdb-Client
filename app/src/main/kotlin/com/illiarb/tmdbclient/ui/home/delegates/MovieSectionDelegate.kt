package com.illiarb.tmdbclient.ui.home.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
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

class MovieSectionDelegate(
  private val bundleStore: SimpleBundleStore,
  private val onSeeAllClickListener: OnClickListener<String>,
  private val onMovieClickListener: OnClickListener<Movie>,
  private val sharedRecycledViewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
) : AdapterDelegate<List<MovieSection>>() {

  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return ViewHolder(
      ItemMovieSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
      bundleStore,
      onSeeAllClickListener,
      sharedRecycledViewPool,
      onMovieClickListener
    )
  }

  override fun isForViewType(items: List<MovieSection>, position: Int): Boolean {
    return items[position] is ListSection
  }

  override fun onBindViewHolder(
    items: List<MovieSection>,
    position: Int,
    holder: RecyclerView.ViewHolder,
    payloads: MutableList<Any>
  ) {
    (holder as ViewHolder).bind(items[position] as ListSection)
  }

  override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
    (holder as ViewHolder).onViewDetachedFromWindow()
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

  private class ViewHolder(
    private val binding: ItemMovieSectionBinding,
    private val bundleStore: SimpleBundleStore,
    private val onSeeAllClickListener: OnClickListener<String>,
    private val sharedRecycledViewPool: RecyclerView.RecycledViewPool,
    onMovieClickListener: OnClickListener<Movie>
  ) : RecyclerView.ViewHolder(binding.root) {

    val adapter = MovieSectionAdapter(onMovieClickListener).apply {
      stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var saveStateKey = SimpleBundleStore.DEFAULT_SAVE_STATE_KEY

    init {
      binding.itemMovieSectionList.let {
        it.adapter = adapter
        it.layoutManager =
          LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        it.setHasFixedSize(true)
        it.setRecycledViewPool(sharedRecycledViewPool)
        it.addItemDecoration(
          SpaceDecoration.edgeInnerSpace(
            it.dimen(R.dimen.spacing_normal),
            it.dimen(R.dimen.spacing_small)
          )
        )
      }
    }

    fun bind(section: ListSection) {
      binding.itemSectionTitle.text = section.title

      adapter.items = section.movies
      adapter.notifyDataSetChanged()

      binding.itemSectionSeeAll.setOnClickListener {
        onSeeAllClickListener(section.code)
      }

      saveStateKey = section.code

      binding.itemMovieSectionList.layoutManager?.onRestoreInstanceState(
        bundleStore.getParcelable(saveStateKey)
      )
    }

    fun onViewDetachedFromWindow() {
      bundleStore.putParcelable(
        saveStateKey,
        binding.itemMovieSectionList.layoutManager?.onSaveInstanceState()
      )
    }
  }
}