package com.illiarb.tmdbclient.modules.home

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentMoviesBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.common.SimpleBundleStore
import com.illiarb.tmdbclient.libs.ui.common.SnackbarController
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.getColorAttr
import com.illiarb.tmdbclient.libs.ui.ext.removeAdapterOnDetach
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.modules.home.HomeViewModel.Event
import com.illiarb.tmdbclient.modules.home.HomeViewModel.State
import com.illiarb.tmdbclient.modules.home.delegates.MovieSectionDelegate
import com.illiarb.tmdbclient.modules.home.delegates.genresSection
import com.illiarb.tmdbclient.modules.home.delegates.nowplaying.nowPlayingSection
import com.illiarb.tmdbclient.modules.home.delegates.trendingSection
import com.illiarb.tmdbclient.modules.home.di.DaggerHomeComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.material.R as MaterialR
import com.illiarb.tmdbclient.libs.ui.R as UiR

class HomeFragment : BaseFragment(R.layout.fragment_movies), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewBinding by viewBinding { fragment ->
    FragmentMoviesBinding.bind(fragment.requireView())
  }

  private val viewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
    ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
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

  override fun inject(appProvider: AppProvider) =
    DaggerHomeComponent.builder()
      .dependencies(appProvider)
      .build()
      .inject(this)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

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
    }

    viewBinding.moviesSwipeRefresh.isEnabled = false

    viewBinding.imageTmdb.setOnClickListener {
      viewModel.events.offer(Event.TmdbIconClick)
    }

    bundleStore.onRestoreInstanceState(savedInstanceState = savedInstanceState)

    setupToolbarScrollListener()
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
      setHasFixedSize(true)
      addItemDecoration(
        SpaceDecoration(
          spacingTopFirst = 0,
          spacingTop = dimen(UiR.dimen.spacing_small),
          spacingBottom = dimen(UiR.dimen.spacing_small),
          spacingBottomLast = dimen(UiR.dimen.spacing_normal)
        )
      )
      isNestedScrollingEnabled = false
      removeAdapterOnDetach()
      doOnApplyWindowInsets { v, insets, initialPadding ->
        v.updatePadding(bottom = initialPadding.bottom + insets.systemWindowInsetBottom)
      }
    }
  }

  @Suppress("MagicNumber")
  private fun setupToolbarScrollListener() {
    viewBinding.moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      val colorEvaluator = ArgbEvaluator()
      val startColor = Color.TRANSPARENT
      val endColor: Int = requireView().getColorAttr(MaterialR.attr.colorPrimary)
      val endStateHeight: Int = requireView().dimen(R.dimen.app_bar_end_state_height)

      val elevationEvaluator = FloatEvaluator()
      val endElevation = requireView().dimen(UiR.dimen.elevation_normal).toFloat()

      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val fraction = calculateFraction(recyclerView.computeVerticalScrollOffset(), endStateHeight)
        val appBarColor = colorEvaluator.evaluate(fraction, startColor, endColor) as Int
        val elevation = elevationEvaluator.evaluate(fraction, 0, endElevation)

        viewBinding.toolbar.setBackgroundColor(appBarColor)
        viewBinding.toolbar.elevation = elevation
      }

      private fun Int.toPercentOf(max: Int): Int = this * 100 / max

      private fun calculateFraction(start: Int, end: Int): Float {
        return start.coerceAtMost(end).toPercentOf(end) / 100f
      }
    })
  }

  private fun render(state: State) {
    viewBinding.moviesSwipeRefresh.isRefreshing = state.sections is Async.Loading<*>

    state.sections.doOnSuccess(adapter::submitList)
    state.error?.let { error ->
      error.consume { message ->
        snackbarController.showMessage(viewBinding.root, message.message)
      }
    }
  }
}