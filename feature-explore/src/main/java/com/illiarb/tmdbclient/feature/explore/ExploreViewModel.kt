package com.illiarb.tmdbclient.feature.explore

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.location.LocationInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreViewModel @Inject constructor(
    private val locationInteractor: LocationInteractor,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val nearbyTheatersSubject = object : DataUiStateSubject<Unit, List<Location>>() {
        override fun createData(payload: Unit): Disposable {
            return locationInteractor.getNearbyMovieTheaters()
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
        }
    }

    fun fetchNearbyMovieTheaters() {
        nearbyTheatersSubject.loadData(Unit)
    }

    fun observeNearbyTheaters(): Observable<UiState<List<Location>>> = nearbyTheatersSubject.observer()
}