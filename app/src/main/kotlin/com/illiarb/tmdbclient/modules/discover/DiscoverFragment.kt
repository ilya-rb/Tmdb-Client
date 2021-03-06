package com.illiarb.tmdbclient.modules.discover

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbclient.databinding.FragmentDiscoverBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.common.SizeSpec
import com.illiarb.tmdbclient.libs.ui.common.SnackbarController
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.getColorAttr
import com.illiarb.tmdbclient.libs.ui.ext.getTintedDrawable
import com.illiarb.tmdbclient.libs.ui.ext.removeAdapterOnDetach
import com.illiarb.tmdbclient.libs.ui.ext.setVisible
import com.illiarb.tmdbclient.libs.ui.ext.textChanges
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.GridDecoration
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.pagination.InfiniteScrollListener
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.pagination.PaginalAdapter
import com.illiarb.tmdbclient.modules.discover.DiscoverViewModel.Event
import com.illiarb.tmdbclient.modules.discover.DiscoverViewModel.State
import com.illiarb.tmdbclient.modules.discover.di.DaggerDiscoverComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.material.R as MaterialR
import com.illiarb.tmdbclient.libs.ui.R as UiR

class DiscoverFragment : BaseFragment(R.layout.fragment_discover), Injectable {

  companion object {
    private const val GRID_SIZE = 3 // 3x3
    private const val SEARCH_DEBOUNCE_DELAY = 400L
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel by viewModels<DiscoverViewModel>(factoryProducer = { viewModelFactory })
  private val viewBinding by viewBinding { fragment ->
    FragmentDiscoverBinding.bind(fragment.requireView())
  }

  private val snackbarController = SnackbarController()
  private val adapter = PaginalAdapter(
    movieDelegate(
      SizeSpec.MatchParent,
      SizeSpec.Fixed(R.dimen.discover_item_movie_height)
    ) {
      viewModel.events.offer(Event.MovieClick(it))
    }
  )

  private val searchResultsAdapter = PaginalAdapter(
    searchResultDelegate {
      viewModel.events.offer(Event.MovieClick(it))
    }
  )

  override fun inject(appProvider: AppProvider) =
    DaggerDiscoverComponent.factory()
      .create(dependencies = appProvider)
      .inject(this)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewBinding.discoverSwipeRefresh.isEnabled = false

    setupToolbar()
    setupSearch()
    setupDiscoverList()
    setupSearchResultsList()

    viewLifecycleOwner.lifecycleScope.launch {
      var oldState: State? = null

      viewModel.state.collect { newState ->
        render(oldState, newState)
        oldState = newState
      }
    }

    ViewCompat.requestApplyInsets(view)
  }

  private fun setupToolbar() {
    viewBinding.appBar.doOnApplyWindowInsets { v, insets, padding ->
      v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
    }

    viewBinding.toolbar.navigationIcon = viewBinding.root.context.getTintedDrawable(
      drawableRes = R.drawable.ic_arrow_back,
      color = viewBinding.root.getColorAttr(MaterialR.attr.colorOnPrimary)
    )

    viewBinding.toolbar.setNavigationOnClickListener {
      viewModel.events.offer(Event.BackClicked)
    }

    viewBinding.discoverFilter.setOnClickListener {
      viewModel.events.offer(Event.FilterClicked)
    }
  }

  private fun setupSearch() {
    viewBinding.discoverSearch.setOnClickListener {
      viewModel.events.offer(Event.SearchClicked)
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewBinding.discoverSearchQuery.textChanges()
        .debounce(SEARCH_DEBOUNCE_DELAY)
        .collect {
          viewModel.events.offer(Event.QueryTextChanged(it))
        }
    }
  }

  private fun setupDiscoverList() {
    val gridLayoutManager = GridLayoutManager(requireContext(), GRID_SIZE).apply {
      spanSizeLookup = createProgressGridSpanSizeLookup()
    }

    viewBinding.discoverList.apply {
      adapter = this@DiscoverFragment.adapter
      layoutManager = gridLayoutManager
      removeAdapterOnDetach()
      setHasFixedSize(true)
      addItemDecoration(GridDecoration(dimen(UiR.dimen.spacing_normal), GRID_SIZE))

      addOnScrollListener(object : InfiniteScrollListener(gridLayoutManager) {
        override fun onLoadMore() {
          viewModel.events.offer(Event.PageEndReached)
        }
      })

      doOnApplyWindowInsets { v, insets, initialPadding ->
        v.updatePadding(bottom = initialPadding.bottom + insets.systemWindowInsetBottom)
      }
    }
  }

  private fun setupSearchResultsList() {
    viewBinding.discoverSearchResults.apply {
      adapter = searchResultsAdapter
      layoutManager = LinearLayoutManager(requireContext())
      removeAdapterOnDetach()
      setHasFixedSize(true)

      addItemDecoration(
        SpaceDecoration.edgeInnerSpaceVertical(
          edgeSpace = dimen(UiR.dimen.spacing_normal),
          innerSpace = dimen(UiR.dimen.spacing_small)
        )
      )

      doOnApplyWindowInsets { v, insets, initialPadding ->
        v.updatePadding(bottom = initialPadding.bottom + insets.systemWindowInsetBottom)
      }
    }
  }

  private fun render(oldState: State?, newState: State) {
    viewBinding.discoverSwipeRefresh.isRefreshing = newState.isLoading
    viewBinding.discoverFilter.apply {
      val count = newState.filter.count()
      setBadgeCount(count)
      setBadgeVisible(count > 0)
    }

    val isDefaultMode = newState.userMode == State.UserMode.Default
    viewBinding.discoverIcons.setVisible(isDefaultMode)
    viewBinding.discoverList.setVisible(isDefaultMode)
    viewBinding.discoverSearchQuery.setVisible(!isDefaultMode)
    viewBinding.discoverSearchResults.setVisible(!isDefaultMode)

    viewBinding.toolbar.title = if (isDefaultMode) {
      getString(R.string.discover_title)
    } else {
      null
    }

    if (isDefaultMode) {
      viewBinding.discoverSearchQuery.clearFocus()
    } else {
      viewBinding.discoverSearchQuery.requestFocus()
    }

    viewBinding.appBar.liftOnScrollTargetViewId = if (isDefaultMode) {
      viewBinding.discoverList.id
    } else {
      viewBinding.discoverSearchResults.id
    }

    if (oldState?.searchResults != newState.searchResults) {
      searchResultsAdapter.update(newState.searchResults, false)
    }

    if (oldState?.results != newState.results) {
      adapter.update(newState.results, newState.isLoadingAdditionalPage)
    }

    newState.errorMessage?.consume { message ->
      snackbarController.showMessage(viewBinding.root, message.message)
    }
  }

  private fun createProgressGridSpanSizeLookup(
    spanCount: Int = GRID_SIZE
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
}