package com.illiarb.tmdbclient.feature.movies.movieslist

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulerProvider: SchedulerProvider,
    private val navigator: Navigator
) : BaseViewModel() {

    private val moviesSubject = object : DataUiStateSubject<String, List<Movie>>() {
        override fun createData(payload: String): Disposable =
            moviesInteractor.getMoviesByType(payload)
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    private val currentFilterSubject = object : DataUiStateSubject<String, String>() {
        override fun createData(payload: String): Disposable =
            Single.just(payload)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    init {
        moviesInteractor.getMovieFilters()
            .subscribe { filters ->
                val (name, code) = filters.first()
                currentFilterSubject.loadData(name)
                moviesSubject.loadData(code)
            }
            .addTo(clearDisposable)
    }

    fun observeMovies(): Observable<UiState<List<Movie>>> = moviesSubject.observer()

    fun observeMovieFilter(): Observable<UiState<String>> = currentFilterSubject.observer()

    fun onMovieClicked(movie: Movie) {
        navigator.showMovieDetailsScreen(movie.id)
    }

    fun onFilterChanged(data: MovieFilter) {
        currentFilterSubject.loadData(data.name)
        moviesSubject.loadData(data.code)
    }
}