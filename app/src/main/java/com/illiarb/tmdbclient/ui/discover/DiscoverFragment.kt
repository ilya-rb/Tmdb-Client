package com.illiarb.tmdbclient.ui.discover

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
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdbclient.ui.delegates.movieDelegate
import com.illiarb.tmdbclient.ui.discover.DiscoverViewModel.Event
import com.illiarb.tmdbclient.ui.discover.DiscoverViewModel.State
import com.illiarb.tmdbclient.ui.discover.di.DaggerDiscoverComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.common.SnackbarController
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.setText
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.GridDecoration
import com.tmdbclient.servicetmdb.domain.Genre
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverFragment : BaseViewBindingFragment<FragmentDiscoverBinding>(), Injectable {

  companion object {
    private const val GRID_SPAN_COUNT = 3
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val adapter = DelegatesAdapter(
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

  override fun inject(appComponent: AppComponent) =
    DaggerDiscoverComponent.builder()
      .dependencies(appComponent)
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

    viewLifecycleOwner.lifecycleScope.launch {
      SnackbarController().bind(binding.root, viewModel.errorState.map { it.message })
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
      viewModel.events.offer(Event.ApplyFilter(binding.discoverFiltersContainer.discoverGenres.checkedChipId))
    }

    binding.root.findViewById<View>(R.id.discoverClearFilter).setOnClickListener {
      binding.discoverFiltersContainer.discoverGenres.clearCheck()
      dismissFiltersPanel()
      viewModel.events.offer(Event.ClearFilter)
    }
  }

  private fun setupDiscoverList() {
    binding.discoverList.apply {
      adapter = this@DiscoverFragment.adapter
      layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
      removeAdapterOnDetach()
      addItemDecoration(GridDecoration(dimen(R.dimen.spacing_normal), GRID_SPAN_COUNT))
    }
  }

  private fun render(oldState: State?, newState: State) {
    binding.discoverSwipeRefresh.isRefreshing = newState.isLoading
    binding.toolbarTitle.setText(newState.screenTitle)

    if (oldState?.genres != newState.genres) {
      newState.genres.forEach { genre ->
        val chip = Chip(
          context,
          null,
          com.illiarb.tmdbexplorer.coreui.R.attr.materialChipChoice
        )

        chip.text = genre.name
        chip.id = genre.id

        binding.discoverFiltersContainer.discoverGenres.addView(chip)
      }
    }

    binding.discoverFiltersContainer.discoverGenres.check(newState.selectedGenreId)

    if (oldState?.results != newState.results) {
      adapter.submitList(newState.results)
    }
  }

  private fun dismissFiltersPanel() {
    binding.discoverRoot.transitionToStart()
  }
}