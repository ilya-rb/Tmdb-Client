package com.illiarb.tmdbclient.feature.movies.list

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulerProvider: SchedulerProvider,
    private val navigator: Navigator
) : BaseViewModel() {

    private val moviesSubject = object : DataUiStateSubject<Unit, List<MovieSection>>() {
        override fun createData(payload: Unit): Disposable =
            moviesInteractor.getMovieSections()
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    init {
        moviesSubject.loadData(Unit)
    }

    fun observeMovies(): Observable<UiState<List<MovieSection>>> = moviesSubject.observer()

    fun onMovieClicked(movie: Movie) {
        navigator.showMovieDetailsScreen(
            movie.id,
            movie.title,
            movie.posterPath
        )
    }
}