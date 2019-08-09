package com.illiarb.tmdbclient.feature.search.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.illiarb.tmdbclient.feature.search.R
import com.illiarb.tmdbclient.feature.search.di.SearchComponent
import com.illiarb.tmdbclient.feature.search.presentation.SearchModel
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState.SearchIcon
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState.SearchResult
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.util.LawObserver
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

/**
 * @author ilya-rb on 26.12.18.
 */
@ExperimentalCoroutinesApi
class SearchFragment : BaseFragment<SearchModel>() {

    @Inject
    lateinit var searchAdapter: SearchAdapter

    private val searchTextWatcher = DebounceTextWatcher {
        presentationModel.search(it)
    }

    private val stateObserver: LawObserver<SearchUiState> by lazy(NONE) {
        LawObserver.create(presentationModel.stateObservable(), ::render)
    }

    override fun getContentView(): Int = R.layout.fragment_search

    override fun inject(appProvider: AppProvider) = SearchComponent.get(appProvider).inject(this)

    override fun getModelClass(): Class<SearchModel> = SearchModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
            .create {
                adapter(searchAdapter)
                type(com.illiarb.core_ui_recycler_view.LayoutType.Linear())
                hasFixedSize(true)
            }
            .setupWith(searchResultsList)

        searchContainer.awareOfWindowInsets()

        searchIcon.setOnClickListener {
            presentationModel.onClearClicked()
        }

        searchFilter.setOnClickListener {
            presentationModel.onSearchFilterClicked()
        }

        searchAdapter.clickEvent = { _, _, item ->
            presentationModel.onMovieClicked(item)
        }

        ViewCompat.requestApplyInsets(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        stateObserver.register(this)
    }

    override fun onStart() {
        super.onStart()
        searchQuery.addTextChangedListener(searchTextWatcher)
    }

    override fun onStop() {
        super.onStop()
        searchQuery.removeTextChangedListener(searchTextWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchResultsList.adapter = null
    }

    private fun render(state: SearchUiState) = runOnUi {
        searchFilter.setVisible(!state.isSearchRunning)
        searchProgress.setVisible(state.isSearchRunning)

        val drawable = when (state.icon) {
            SearchIcon.Search ->
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
            SearchIcon.Cross ->
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear_search)
        }

        searchIcon.setImageDrawable(drawable)

        // If now icon are search search query was cleared
        if (state.icon is SearchIcon.Search) {
            searchQuery.setText("")
        }

        when (state.result) {
            is SearchResult.Success -> {
                searchAdapter.submitList(state.result.movies)

                searchResultsList.show()
                emptyView.hide()
            }

            else -> {
                searchResultsList.hide()
                emptyView.show()

                when (state.result) {
                    SearchResult.Initial ->
                        emptyViewText.text = getString(R.string.search_initial)
                    SearchResult.Empty ->
                        emptyViewText.text = getString(R.string.search_no_results_found)
                }
            }
        }
    }
}