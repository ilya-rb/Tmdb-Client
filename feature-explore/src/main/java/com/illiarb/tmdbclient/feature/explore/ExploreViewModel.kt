package com.illiarb.tmdbclient.feature.explore

import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.modules.location.LocationInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import javax.inject.Inject

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreViewModel @Inject constructor(
    private val locationInteractor: LocationInteractor,
    private val schedulerProvider: SchedulerProvider
): BaseViewModel() {

    fun fetchNearbyMovieTheaters() {

    }

}