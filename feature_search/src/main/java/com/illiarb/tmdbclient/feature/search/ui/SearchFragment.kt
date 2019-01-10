package com.illiarb.tmdbclient.feature.search.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.illiarb.tmdbclient.feature.search.DebounceTextWatcher
import com.illiarb.tmdbclient.feature.search.R
import com.illiarb.tmdbclient.feature.search.SearchModel
import com.illiarb.tmdbclient.feature.search.SearchUiState
import com.illiarb.tmdbclient.feature.search.SearchUiState.SearchIcon
import com.illiarb.tmdbclient.feature.search.SearchUiState.SearchResult
import com.illiarb.tmdbclient.feature.search.di.SearchComponent
import com.illiarb.tmdbexplorer.coreui.StateObserver
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class SearchFragment : BaseFragment<SearchModel>(), Injectable, StateObserver<SearchUiState> {

    @Inject
    lateinit var searchAdapter: SearchAdapter

    private val searchTextWatcher = DebounceTextWatcher {
        presentationModel.search(it)
    }

    override fun getContentView(): Int = R.layout.fragment_search

    override fun inject(appProvider: AppProvider) = SearchComponent.get(appProvider).inject(this)

    override fun getModelClass(): Class<SearchModel> = SearchModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RecyclerViewBuilder
            .create {
                adapter(searchAdapter)
                type(LayoutType.Linear())
                hasFixedSize(true)
            }
            .setupWith(searchResultsList)

        searchIcon.setOnClickListener {
            presentationModel.onClearClicked()
        }

        searchAdapter.clickEvent = { _, _, item ->
            presentationModel.onMovieClicked(item)
        }
    }

    override fun onStart() {
        super.onStart()
        presentationModel.observeState(this)
        searchQuery.addTextChangedListener(searchTextWatcher)
    }

    override fun onStop() {
        super.onStop()
        presentationModel.stopObserving(this)
        searchQuery.removeTextChangedListener(searchTextWatcher)
    }

    override fun onStateChanged(state: SearchUiState) {
        searchProgress.visibility = if (state.isSearchRunning) View.VISIBLE else View.GONE

        val drawable = when (state.icon) {
            SearchIcon.Search -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
            SearchIcon.Cross -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear_search)
        }

        searchIcon.setImageDrawable(drawable)

        // If now icon are search search query was cleared
        if (state.icon is SearchIcon.Search) {
            searchQuery.setText("")
        }

        when (state.result) {
            is SearchResult.Success -> {
                searchAdapter.submitList(state.result.movies)

                searchResultsList.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }

            else -> {
                searchResultsList.visibility = View.GONE
                emptyView.visibility = View.VISIBLE

                when (state.result) {
                    is SearchResult.Initial -> emptyViewText.text = getString(R.string.search_initial)
                    is SearchResult.Empty -> emptyViewText.text = getString(R.string.search_no_results_found)
                }
            }
        }
    }
}