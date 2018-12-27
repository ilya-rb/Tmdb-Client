package com.illiarb.tmdbclient.feature.search.viewmodel

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.feature.search.SearchIntent
import com.illiarb.tmdbclient.feature.search.SearchView
import com.illiarb.tmdbclient.feature.search.SearchViewState
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor.SearchResult
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val clearDisposable = CompositeDisposable()

    private val reducer = BiFunction<SearchViewState, SearchResult, SearchViewState> { previous, result ->
        when (result) {
            is SearchResult.Loading -> previous.copy(isSearchRunning = true)
            is SearchResult.Success -> previous.copy(isSearchRunning = false, searchResults = result.movies)
            is SearchResult.Failure -> previous.copy(isSearchRunning = false, error = result.error)
        }
    }

    fun bind(view: SearchView) {
        view.intents
            .map { intentToCommand(it) }
            .subscribeOn(schedulerProvider.provideIoScheduler())
            .subscribe(searchInteractor.command)
            .addTo(clearDisposable)

        searchInteractor.sideEffects
            .observeOn(schedulerProvider.provideMainThreadScheduler())
            .subscribe(view.sideEffects)
            .addTo(clearDisposable)

        searchInteractor.results
            .observeOn(schedulerProvider.provideMainThreadScheduler())
            .scan(SearchViewState.idle(), reducer)
            .subscribe(view.state)
            .addTo(clearDisposable)
    }

    fun unbind() {
        clearDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        unbind()
    }

    private fun intentToCommand(intent: SearchIntent): SearchInteractor.Command =
        when (intent) {
            is SearchIntent.Search -> SearchInteractor.Command.Search(intent.query)
            is SearchIntent.ShowMovieDetails -> SearchInteractor.Command.SearchMovieDetails(intent.id)
        }
}