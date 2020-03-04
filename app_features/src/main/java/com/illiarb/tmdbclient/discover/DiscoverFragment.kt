package com.illiarb.tmdbclient.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.transition.MaterialFadeThrough
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbclient.discover.DiscoverViewModel.State
import com.illiarb.tmdbclient.discover.di.DiscoverComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentDiscoverBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.common.Text
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverFragment : BaseViewBindingFragment<FragmentDiscoverBinding>(), Injectable {

  companion object {
    private const val GRID_SPAN_COUNT = 3
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var discoverGenres: ChipGroup
  private lateinit var filtersContainer: ViewGroup
  private lateinit var discoverSwipeArea: View

  private val adapter = DelegatesAdapter(
    movieDelegate(
      SizeSpec.MatchParent,
      SizeSpec.Fixed(R.dimen.discover_item_movie_height),
      clickListener = {
        viewModel.events.offer(DiscoverViewModel.Event.MovieClick(it))
      }
    )
  )

  private val viewModel: DiscoverViewModel by lazy(LazyThreadSafetyMode.NONE) {
    ViewModelProvider(this, viewModelFactory).get(DiscoverViewModel::class.java)
  }

  override fun inject(appProvider: AppProvider) {
    val id = arguments?.getInt(ShowDiscover.EXTRA_GENRE_ID, Genre.GENRE_ALL) ?: Genre.GENRE_ALL
    DiscoverComponent.get(appProvider, id).inject(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enterTransition = MaterialFadeThrough.create(requireContext())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // filters layout added via <include> tag and not supported by view binding
    discoverGenres = binding.root.findViewById(R.id.discoverGenres)
    discoverSwipeArea = binding.root.findViewById(R.id.discoverSwipeArea)
    filtersContainer = binding.root.findViewById(R.id.discoverFiltersContainer)

    binding.discoverSwipeRefresh.isEnabled = false

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
    discoverSwipeArea.setOnClickListener {
      showFiltersPanel()
    }

    binding.root.findViewById<View>(R.id.discoverApplyFilter).setOnClickListener {
      dismissFiltersPanel()
      viewModel.events.offer(DiscoverViewModel.Event.ApplyFilter(discoverGenres.checkedChipId))
    }

    binding.root.findViewById<View>(R.id.discoverClearFilter).setOnClickListener {
      discoverGenres.clearCheck()
      dismissFiltersPanel()
      viewModel.events.offer(DiscoverViewModel.Event.ClearFilter)
    }
  }

  private fun setupDiscoverList() {
    binding.discoverList.apply {
      adapter = this@DiscoverFragment.adapter
      layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
      removeAdapterOnDetach()

      val spacing = dimen(R.dimen.spacing_small)
      addItemDecoration(SpaceDecoration(spacing, spacing, spacing, spacing))
    }
  }

  private fun render(oldState: State?, newState: State) {
    binding.discoverSwipeRefresh.isRefreshing = newState.isLoading
    binding.toolbar.title = when (newState.screenTitle) {
      is Text.AsString -> newState.screenTitle.text
      is Text.AsResource -> getString(newState.screenTitle.id)
    }

    if (oldState?.selectedGenreId != newState.selectedGenreId) {
      discoverGenres.check(newState.selectedGenreId)
    }

    if (oldState?.genres != newState.genres) {
      newState.genres.forEach { genre ->
        val chip = Chip(
          context,
          null,
          com.illiarb.tmdbexplorer.coreui.R.attr.materialChipChoice
        )

        chip.text = genre.name
        chip.id = genre.id

        discoverGenres.addView(chip)
      }
    }

    if (oldState?.results != newState.results) {
      adapter.submitList(newState.results)
    }
  }

  private fun showFiltersPanel() {
    binding.discoverRoot.transitionToEnd()
  }

  private fun dismissFiltersPanel() {
    binding.discoverRoot.transitionToStart()
  }
}