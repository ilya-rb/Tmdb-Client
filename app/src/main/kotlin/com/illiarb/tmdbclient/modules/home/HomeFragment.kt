package com.illiarb.tmdbclient.modules.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentMoviesBinding
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.common.SimpleBundleStore
import com.illiarb.tmdbclient.libs.ui.common.SnackbarController
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.getColorAttr
import com.illiarb.tmdbclient.libs.ui.ext.removeAdapterOnDetach
import com.illiarb.tmdbclient.libs.ui.ext.tintMenuItemsWithColor
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding
import com.illiarb.tmdbclient.libs.ui.widget.ToolbarScrollListener
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.modules.home.HomeViewModel.Event
import com.illiarb.tmdbclient.modules.home.HomeViewModel.State
import com.illiarb.tmdbclient.modules.home.delegates.MovieSectionDelegate
import com.illiarb.tmdbclient.modules.home.delegates.genresSection
import com.illiarb.tmdbclient.modules.home.delegates.nowplaying.nowPlayingSection
import com.illiarb.tmdbclient.modules.home.delegates.trendingSection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.material.R as MaterialR
import com.illiarb.tmdbclient.libs.ui.R as UiR

class HomeFragment @Inject constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(R.layout.fragment_movies) {

  private val viewModel by viewModels<HomeViewModel>(factoryProducer = { viewModelFactory })
  private val viewBinding by viewBinding { fragment ->
    FragmentMoviesBinding.bind(fragment.requireView())
  }

  private val snackbarController = SnackbarController()
  private val bundleStore = SimpleBundleStore()
  private val adapter = DelegatesAdapter(
    MovieSectionDelegate(
      bundleStore,
      onSeeAllClickListener = {
        viewModel.events.offer(Event.SeeAllClick(it))
      },
      onMovieClickListener = {
        viewModel.events.offer(Event.MovieClick(it))
      }
    ),
    nowPlayingSection(bundleStore) {
      viewModel.events.offer(Event.MovieClick(it))
    },
    genresSection {
      viewModel.events.offer(Event.GenreClick(it))
    },
    trendingSection(bundleStore) {
      viewModel.events.offer(Event.MovieClick(it))
    }
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewBinding.moviesSwipeRefresh.isEnabled = false
    viewBinding.imageTmdb.setOnClickListener {
      viewModel.events.offer(Event.TmdbIconClick)
    }

    bundleStore.onRestoreInstanceState(savedInstanceState = savedInstanceState)

    setupToolbar()
    setupMoviesList()

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.state.collect {
        render(it)
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    bundleStore.onSaveInstanceState(outState = outState)
  }

  private fun setupMoviesList() {
    viewBinding.moviesList.apply {
      adapter = this@HomeFragment.adapter
      layoutManager = LinearLayoutManager(requireContext())
      isNestedScrollingEnabled = false
      setHasFixedSize(true)
      addItemDecoration(
        SpaceDecoration(
          spacingTopFirst = 0,
          spacingTop = dimen(UiR.dimen.spacing_small),
          spacingBottom = dimen(UiR.dimen.spacing_small),
          spacingBottomLast = dimen(UiR.dimen.spacing_normal)
        )
      )

      removeAdapterOnDetach()

      doOnApplyWindowInsets { v, insets, initialPadding ->
        v.updatePadding(bottom = initialPadding.bottom + insets.systemWindowInsetBottom)
      }

      addOnScrollListener(ToolbarScrollListener(
        endColor = requireView().getColorAttr(MaterialR.attr.colorPrimary),
        distance = requireView().dimen(R.dimen.app_bar_end_state_height),
        endElevation = requireView().dimen(UiR.dimen.elevation_normal).toFloat()
      ) { elevation, color ->
        viewBinding.toolbar.setBackgroundColor(color.value)
        viewBinding.toolbar.elevation = elevation.value
      })
    }
  }

  private fun setupToolbar() {
    viewBinding.toolbar.apply {
      setOnMenuItemClickListener {
        when (it.itemId) {
          R.id.menu_home_debug -> viewModel.events.offer(Event.DebugClick)
          R.id.menu_home_discover -> viewModel.events.offer(Event.DiscoverClick)
        }
        true
      }

      doOnApplyWindowInsets { v, insets, padding ->
        v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
      }

      menu.tintMenuItemsWithColor(
        viewBinding.root.getColorAttr(MaterialR.attr.colorOnPrimary)
      )
    }
  }

  private fun render(state: State) {
    viewBinding.moviesSwipeRefresh.isRefreshing = state.sections is Async.Loading

    state.sections.doOnSuccess {
      adapter.submitList(it)
    }

    state.error?.consume { message ->
      snackbarController.showMessage(viewBinding.root, message.message)
    }
  }
}