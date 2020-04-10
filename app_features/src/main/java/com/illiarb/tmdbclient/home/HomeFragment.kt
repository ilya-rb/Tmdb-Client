package com.illiarb.tmdbclient.home

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
import com.illiarb.tmdbclient.home.HomeViewModel.Event
import com.illiarb.tmdbclient.home.HomeViewModel.State
import com.illiarb.tmdbclient.home.delegates.genresSection
import com.illiarb.tmdbclient.home.delegates.movieSection
import com.illiarb.tmdbclient.home.delegates.nowplaying.nowPlayingSection
import com.illiarb.tmdbclient.home.delegates.trendingSection
import com.illiarb.tmdbclient.home.di.HomeComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMoviesBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.common.SnackbarController
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.getColorAttr
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.common.SimpleBundleStore
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.util.Async
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
    nowPlayingSection(bundleStore) { viewModel.events.offer(Event.MovieClick(it)) },
    genresSection { viewModel.events.offer(Event.GenreClick(it)) },
    trendingSection(bundleStore) { viewModel.events.offer(Event.MovieClick(it)) },
    movieSection(
      bundleStore,
      seeAllClickListener = { viewModel.events.offer(Event.SeeAllClick(it)) },
      movieClickListener = { viewModel.events.offer(Event.MovieClick(it)) }
    )
  )

  override fun getViewBinding(inflater: LayoutInflater): FragmentMoviesBinding =
    FragmentMoviesBinding.inflate(inflater)

  override fun inject(appProvider: AppProvider) = HomeComponent.get(appProvider).inject(this)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.appBar.doOnApplyWindowInsets { v, insets, padding ->
      v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
    }

    binding.moviesSwipeRefresh.isEnabled = false
    binding.settings.setOnClickListener {
      viewModel.events.offer(Event.SettingsClick)
    }

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
    binding.moviesSwipeRefresh.isRefreshing = state.sections is Async.Loading
    state.sections.doOnSuccess(adapter::submitList)
  }
}