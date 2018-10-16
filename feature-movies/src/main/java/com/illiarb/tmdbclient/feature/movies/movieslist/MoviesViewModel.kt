package com.illiarb.tmdbclient.feature.movies.movieslist

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.ShowMovieDetailsAction
import com.illiarb.tmdblcient.core.schedulers.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    moviesInteractor: MoviesInteractor,
    private val schedulerProvider: SchedulerProvider,
    private val showMovieDetailsAction: ShowMovieDetailsAction
) : BaseViewModel() {

    private val moviesSubject = object : DataUiStateSubject<String, List<Movie>>() {
        override fun createData(payload: String): Disposable {
            return moviesInteractor.getMoviesByType(payload)
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
        }
    }

    init {
        moviesSubject.loadData("now_playing")
    }

    fun observeMovies(): Observable<UiState<List<Movie>>> = moviesSubject.observer()

    fun onMovieClicked(movie: Movie) {
        showMovieDetailsAction.showMovieDetails(movie.id)
    }
}