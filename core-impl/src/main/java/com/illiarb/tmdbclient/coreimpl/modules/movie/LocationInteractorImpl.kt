package com.illiarb.tmdbclient.coreimpl.modules.movie

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

    override fun getNearbyMovieTheaters(coords: Location): Single<List<Location>> =
        locationRepository.getCurrentLocation()
            .flatMap {
                locationRepository.getNearbyMovieTheaters(it)
            }
}