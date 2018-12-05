package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.location.AndroidLocationService
import com.illiarb.tmdbclient.storage.mappers.LocationMapper
import com.illiarb.tmdbclient.storage.mappers.here.HereLocationMapper
import com.illiarb.tmdbclient.storage.network.hereapi.HereApiService
import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.modules.explore.LocationRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 01.11.18.
 */
class LocationRepositoryImpl @Inject constructor(
    private val locationService: AndroidLocationService,
    private val locationMapper: LocationMapper,
    private val hereApiService: HereApiService,
    private val hereLocationMapper: HereLocationMapper
) : LocationRepository {

    companion object {
        val FAKE_LOCATION = Location("My location", 50.4390483, 30.4966947, 0)
    }

    override fun getCurrentLocation(): Single<Location> = Single.just(FAKE_LOCATION)

    override fun getNearbyMovieTheaters(coords: Location): Single<List<Location>> =
        hereApiService.getNearbyMovieTheaters("${coords.lat},${coords.lon}")
            .map { it.results }
            .map { it.items }
            .map(hereLocationMapper::mapList)
}