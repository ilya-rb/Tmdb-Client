package com.illiarb.tmdbclient.movies.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.di.MoviesComponent
import com.illiarb.tmdbclient.movies.home.HomeView.*
import com.illiarb.tmdbclient.movies.home.HomeView.SingleUiEvent.ClearSearch
import com.illiarb.tmdbclient.movies.home.HomeView.SingleUiEvent.MovieClick
import com.illiarb.tmdbclient.movies.home.adapter.MovieAdapter
import com.illiarb.tmdbclient.movies.home.adapter.SearchResultsAdapter
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.common.BackPressedListener
import com.illiarb.tmdbexplorer.coreui.ext.hideKeyboard
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeFragment : BaseFragment(), Injectable, HomeView, BackPressedListener {

    @Inject
    lateinit var adapter: MovieAdapter

    @Inject
    lateinit var searchResultsAdapter: SearchResultsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory.create(HomeModel::class.java)
    }

    private val uiEventsChannel = Channel<SingleUiEvent>()

    override val coroutineContext: CoroutineContext
        get() = lifecycleScope.coroutineContext

    override val searchQuery: Flow<CharSequence>
        get() = searchEditText.textChanges()

    override val searchFocusChange: Flow<Boolean>
        get() = searchEditText.focusChanges()

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun inject(appProvider: AppProvider) =
        MoviesComponent.get(appProvider, requireActivity()).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar.isLiftOnScroll = true
        appBar.liftOnScrollTargetViewId = R.id.moviesList

        adapter.onClickBlock = {
            lifecycleScope.launch {
                uiEventsChannel.send(MovieClick(it))
            }
        }

        searchClose.setOnClickListener {
            searchEditText.setText("")
            searchEditText.hideKeyboard()
            searchEditText.clearFocus()
        }

        RecyclerViewBuilder.create {
            adapter(adapter)
            type(LayoutType.Linear())
            hasFixedSize(true)
            spaceBetween { spacing = resources.getDimensionPixelSize(R.dimen.item_movie_spacing) }
        }.setupWith(moviesList)

        viewModel.bind(this)
    }

    override fun onBackPressed(): Boolean {
        if (searchEditText.hasFocus()) {
            searchEditText.setText("")
            searchEditText.clearFocus()
            return true
        }
        return false
    }

    override val uiEvents: Flow<SingleUiEvent>
        get() = uiEventsChannel.consumeAsFlow()

    override fun movieSections(state: MovieSectionsState) {
        when (state) {
            is MovieSectionsState.Loading -> {
                // TODO: Loading state
            }

            is MovieSectionsState.Failure -> {
                // TODO: Error state
            }

            is MovieSectionsState.Content -> {
                adapter.items = state.sections
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun searchResultsState(state: SearchResultState) {
        searchClose.setVisible(state !is SearchResultState.Hidden)

        if (state is SearchResultState.Hidden) {
            moviesList.adapter = adapter
            return
        }

        if (moviesList.adapter != searchResultsAdapter) {
            moviesList.adapter = searchResultsAdapter
        }

        when (state) {
            is SearchResultState.Empty -> {
                searchResultsAdapter.items = Collections.emptyList()
                searchResultsAdapter.notifyDataSetChanged()
            }

            is SearchResultState.Results -> {
                searchResultsAdapter.items = state.results
                searchResultsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun accountVisible(visible: Boolean) = account.setVisible(visible)
}