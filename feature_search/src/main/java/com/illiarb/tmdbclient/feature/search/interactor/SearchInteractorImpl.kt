package com.illiarb.tmdbclient.feature.search.interactor

import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor.SearchResult
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class SearchInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository
) : SearchInteractor {

    private val resultsSubject = BehaviorSubject.create<SearchResult>()

    private val sideEffectSubject = PublishSubject.create<SearchInteractor.SideEffect>()

    private val disposables = CompositeDisposable()

    override val results: Observable<SearchInteractor.SearchResult>
        get() = resultsSubject
            .hide()
            .doOnDispose { disposables.clear() }

    override val sideEffects: Observable<SearchInteractor.SideEffect>
        get() = sideEffectSubject.hide()

    override val command: Consumer<SearchInteractor.Command>
        get() = Consumer { command ->
            when (command) {
                is SearchInteractor.Command.SearchMovieDetails ->
                    sideEffectSubject.onNext(SearchInteractor.SideEffect.ShowScreen(MovieDetailsScreen(command.id)))

                is SearchInteractor.Command.Search ->
                    moviesRepository.searchMovies(command.query)
                        .toObservable()
                        .map(SearchResult::Success)
                        .cast(SearchResult::class.java)
                        .startWith(SearchResult.Loading)
                        .onErrorReturn(SearchResult::Failure)
                        .subscribe { result ->
                            resultsSubject.onNext(result)

                            hasSideEffects(result)?.let {
                                sideEffectSubject.onNext(it)
                            }
                        }
                        .addTo(disposables)
            }
        }

    private fun hasSideEffects(result: SearchResult): SearchInteractor.SideEffect? =
        when (result) {
            is SearchResult.Failure -> SearchInteractor.SideEffect.ShowError(result.error.message!!)
            else -> null
        }
}