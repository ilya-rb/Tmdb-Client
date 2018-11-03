package com.illiarb.tmdbclient.coreimpl.explore

import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.modules.location.LocationInteractor
import com.illiarb.tmdblcient.core.modules.location.LocationRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 02.11.18.
 */
class LocationInteractorImpl @Inject constructor(
    private val locationRepository: LocationRepository
) : LocationInteractor {

    companion object {
        val FAKE_LOCATION = Location(50.4390483, 30.4966947)
    }

    override fun getNearbyMovieTheaters(): Single<List<Location>> =
        locationRepository.getNearbyMovieTheaters(FAKE_LOCATION)
//        locationRepository.getCurrentLocation()
//            .observeOn(schedulerProvider.provideIoScheduler())
//            .flatMap {
//                locationRepository.getNearbyMovieTheaters(it)
//            }
}