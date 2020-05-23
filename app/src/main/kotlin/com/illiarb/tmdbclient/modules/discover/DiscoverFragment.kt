package com.illiarb.tmdbclient.modules.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentDiscoverBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseViewBindingFragment
import com.illiarb.tmdbclient.libs.ui.common.SizeSpec
import com.illiarb.tmdbclient.libs.ui.common.SnackbarController
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.removeAdapterOnDetach
import com.illiarb.tmdbclient.libs.ui.ext.setText
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.GridDecoration
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.pagination.InfiniteScrollListener
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.pagination.PaginalAdapter
import com.illiarb.tmdbclient.modules.delegates.movieDelegate
import com.illiarb.tmdbclient.modules.discover.DiscoverViewModel.Event
import com.illiarb.tmdbclient.modules.discover.DiscoverViewModel.State
import com.illiarb.tmdbclient.modules.discover.di.DaggerDiscoverComponent
import com.illiarb.tmdbclient.navigation.Action.ShowDiscover
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.illiarb.tmdbclient.libs.ui.R as UiR

class DiscoverFragment : BaseViewBindingFragment<FragmentDiscoverBinding>(), Injectable {

  companion object {
    private const val GRID_SPAN_COUNT = 3
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val snackbarController = SnackbarController()
  private val adapter = PaginalAdapter(
    movieDelegate(
      SizeSpec.MatchParent,
      SizeSpec.Fixed(R.dimen.discover_item_movie_height)
    ) {
      viewModel.events.offer(Event.MovieClick(it))
    }
  )

  private val viewModel: DiscoverViewModel by lazy(LazyThreadSafetyMode.NONE) {
    ViewModelProvider(this, viewModelFactory).get(DiscoverViewModel::class.java)
  }

  override fun inject(appProvider: AppProvider) =
    DaggerDiscoverComponent.builder()
      .dependencies(appProvider)
      .genreId(arguments?.getInt(ShowDiscover.EXTRA_GENRE_ID, Genre.GENRE_ALL) ?: Genre.GENRE_ALL)
      .build()
      .inject(this)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.discoverSwipeRefresh.isEnabled = false
    binding.discoverRoot.setTransitionListener(object : TransitionAdapter() {
      override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        binding.discoverOverlay.isClickable = currentId == R.id.filtersEnd
      }
    })

    binding.discoverOverlay.setOnClickListener { binding.discoverRoot.transitionToStart() }
    binding.discoverOverlay.isClickable = false

    setupToolbar()
    setupFilters()
    setupDiscoverList()

    viewLifecycleOwner.lifecycleScope.launch {
      var oldState: State? = null

      viewModel.state.collect { newState ->
        render(oldState, newState)
        oldState = newState
      }
    }

    ViewCompat.requestApplyInsets(view)
  }

  override fun getViewBinding(inflater: LayoutInflater): FragmentDiscoverBinding =
    FragmentDiscoverBinding.inflate(inflater)

  private fun setupToolbar() {
    binding.toolbar.setNavigationOnClickListener {
      activity?.onBackPressed()
    }
  }

  private fun setupFilters() {
    binding.root.findViewById<View>(R.id.discoverApplyFilter).setOnClickListener {
      dismissFiltersPanel()
      viewModel.events.offer(
        Event.ApplyFilter(
          binding.discoverFiltersContainer.discoverGenres.checkedChipIds
        )
      )
    }

    binding.root.findViewById<View>(R.id.discoverClearFilter).setOnClickListener {
      binding.discoverFiltersContainer.discoverGenres.clearCheck()
      dismissFiltersPanel()
      viewModel.events.offer(Event.ClearFilter)
    }
  }

  private fun setupDiscoverList() {
    binding.discoverList.apply {
      val gridLayoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        .apply { spanSizeLookup = createProgressGridSpanSizeLookup() }
      adapter = this@DiscoverFragment.adapter
      layoutManager = gridLayoutManager
      removeAdapterOnDetach()
      addItemDecoration(GridDecoration(dimen(UiR.dimen.spacing_normal), GRID_SPAN_COUNT))
      addOnScrollListener(object : InfiniteScrollListener(gridLayoutManager) {
        override fun onLoadMore() {
          viewModel.events.offer(Event.PageEndReached)
        }
      })
    }
  }

  private fun render(oldState: State?, newState: State) {
    binding.discoverSwipeRefresh.isRefreshing = newState.isLoading
    binding.toolbarTitle.setText(newState.screenTitle)

    if (oldState?.genres != newState.genres) {
      newState.genres.forEach { genre ->
        val chip = createChipFromGenre(genre)
        binding.discoverFiltersContainer.discoverGenres.addView(chip)
      }
    }

    if (newState.selectedGenreIds.isEmpty()) {
      binding.discoverFiltersContainer.discoverGenres.clearCheck()
    } else {
      newState.selectedGenreIds.forEach {
        binding.discoverFiltersContainer.discoverGenres.check(it)
      }
    }

    adapter.update(newState.results, newState.isLoadingAdditionalPage)

    newState.errorMessage?.let { error ->
      error.consume { message ->
        snackbarController.showMessage(binding.root, message.message)
      }
    }
  }

  private fun createProgressGridSpanSizeLookup(
    spanCount: Int = GRID_SPAN_COUNT
  ): GridLayoutManager.SpanSizeLookup {
    return object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int =
        if (position == adapter.itemCount - 1) {
          spanCount
        } else {
          1
        }
    }
  }

  private fun createChipFromGenre(genre: Genre): Chip {
    return Chip(
      context,
      null,
      com.illiarb.tmdbclient.libs.ui.R.attr.materialChipChoice
    ).also {
      it.text = genre.name
      it.id = genre.id
    }
  }

  private fun dismissFiltersPanel() {
    binding.discoverRoot.transitionToStart()
  }
}