package com.illiarb.tmdbclient.feature.movies.movieslist.filters

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author ilya-rb on 31.10.18.
 */
class MovieFiltersViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulersProvider: SchedulerProvider
) : BaseViewModel() {

    private val filtersSubject = object : DataUiStateSubject<Unit, List<MovieFilter>>() {
        override fun createData(payload: Unit): Disposable =
            moviesInteractor.getMovieFilters()
                .ioToMain(schedulersProvider)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    init {
        filtersSubject.loadData(Unit)
    }

    fun observeMovieFilters(): Observable<UiState<List<MovieFilter>>> = filtersSubject.observer()
}