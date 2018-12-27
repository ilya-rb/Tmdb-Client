package com.illiarb.tmdbclient.feature.search.interactor

import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor.SearchResult
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class SearchInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val router: Router
) : SearchInteractor {

    private val searchSubject = BehaviorSubject.create<SearchResult>()

    override val results: Observable<SearchInteractor.SearchResult>
        get() = searchSubject.hide()

    override val sideEffects: Observable<SearchInteractor.SideEffect>
        get() = Observable.empty()

    override val command: Consumer<SearchInteractor.Command>
        get() = Consumer { command ->
            when (command) {
                is SearchInteractor.Command.Search ->
                    moviesRepository.searchMovies(command.query)
                        .toObservable()
                        .map(SearchResult::Success)
                        .cast(SearchResult::class.java)
                        .startWith(SearchResult.Loading)
                        .onErrorReturn(SearchResult::Failure)
                        .subscribe(searchSubject)

                is SearchInteractor.Command.SearchMovieDetails -> router.navigateTo(MovieDetailsScreen(command.id))
            }
        }
}