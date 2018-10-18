package com.illiarb.tmdbclient.feature.movies.details

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.schedulers.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    var id: Int = 0
        set(value) {
            if (value != 0) {
                field = value
                movieDetailsState.loadData(value)
            }
        }

    private val movieDetailsState = object : DataUiStateSubject<Int, Movie>() {
        override fun createData(payload: Int): Disposable =
            moviesInteractor.getMovieDetails(payload, "")
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    fun observeMovieDetailsState(): Observable<UiState<Movie>> = movieDetailsState.observer()
}