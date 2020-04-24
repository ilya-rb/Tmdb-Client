package com.illiarb.tmdbclient.ui.home

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentMoviesBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseViewBindingFragment
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
import com.illiarb.tmdbclient.ui.home.HomeViewModel.Event
import com.illiarb.tmdbclient.ui.home.HomeViewModel.State
import com.illiarb.tmdbclient.ui.home.delegates.MovieSectionDelegate
import com.illiarb.tmdbclient.ui.home.delegates.genresSection
import com.illiarb.tmdbclient.ui.home.delegates.nowplaying.nowPlayingSection
import com.illiarb.tmdbclient.ui.home.delegates.trendingSection
import com.illiarb.tmdbclient.ui.home.di.DaggerHomeComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseViewBindingFragment<FragmentMoviesBinding>(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
    ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
  }

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

  override fun getViewBinding(inflater: LayoutInflater): FragmentMoviesBinding =
    FragmentMoviesBinding.inflate(inflater)

  override fun inject(appProvider: AppProvider) =
    DaggerHomeComponent.builder()
      .dependencies(appProvider)
      .build()
      .inject(this)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.appBar.doOnApplyWindowInsets { v, insets, padding ->
      v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
    }

    binding.moviesSwipeRefresh.isEnabled = false

    bundleStore.onRestoreInstanceState(savedInstanceState = savedInstanceState)

    setupAppBarScrollListener()
    setupMoviesList()

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.state.collect {
        render(it)
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      SnackbarController().bind(binding.root, viewModel.errorState.map { it.message })
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    bundleStore.onSaveInstanceState(outState = outState)
  }

  private fun setupMoviesList() {
    binding.moviesList.apply {
      adapter = this@HomeFragment.adapter
      layoutManager = LinearLayoutManager(requireContext())
      addItemDecoration(
        SpaceDecoration(
          spacingTopFirst = 0,
          spacingTop = dimen(R.dimen.spacing_small),
          spacingBottom = dimen(R.dimen.spacing_small),
          spacingBottomLast = dimen(R.dimen.spacing_normal)
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
  private fun setupAppBarScrollListener() {
    binding.moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

      val colorEvaluator = ArgbEvaluator()
      val startColor = Color.TRANSPARENT
      val endColor: Int = requireView().getColorAttr(R.attr.colorPrimary)
      val endStateHeight: Int = requireView().dimen(R.dimen.app_bar_end_state_height)

      val elevationEvaluator = FloatEvaluator()
      val endElevation = requireView().dimen(R.dimen.elevation_normal).toFloat()

      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val fraction = calculateFraction(recyclerView.computeVerticalScrollOffset(), endStateHeight)
        val appBarColor = colorEvaluator.evaluate(fraction, startColor, endColor) as Int
        val elevation = elevationEvaluator.evaluate(fraction, 0, endElevation)

        binding.appBar.setBackgroundColor(appBarColor)
        binding.appBar.elevation = elevation
      }

      private fun Int.toPercentOf(max: Int): Int = this * 100 / max

      private fun calculateFraction(start: Int, end: Int): Float {
        return start.coerceAtMost(end).toPercentOf(end) / 100f
      }
    })
  }

  private fun render(state: State) {
    binding.moviesSwipeRefresh.isRefreshing = state.sections is Async.Loading<*>
    state.sections.doOnSuccess(adapter::submitList)
  }
}