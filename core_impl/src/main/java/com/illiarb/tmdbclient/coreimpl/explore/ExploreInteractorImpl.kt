package com.illiarb.tmdbclient.coreimpl.explore

import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.modules.explore.ExploreInteractor
import com.illiarb.tmdblcient.core.modules.explore.LocationRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 02.11.18.
 */
class ExploreInteractorImpl @Inject constructor(
    private val locationRepository: LocationRepository
) : ExploreInteractor {

    override fun getNearbyMovieTheaters(): Single<List<Location>> =
        locationRepository.getCurrentLocation()
            .flatMap { locationRepository.getNearbyMovieTheaters(it) }
            .map { theaters ->
                theaters.sortedBy { theater ->
                    theater.distance
                }
            }
}