package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.location.AndroidLocationService
import com.illiarb.tmdbclient.storage.mappers.LocationMapper
import com.illiarb.tmdbclient.storage.mappers.here.HereLocationMapper
import com.illiarb.tmdbclient.storage.network.hereapi.HereApiService
import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.storage.LocationRepository
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

    override fun getCurrentLocation(): Single<Location> =
        locationService.getLastKnownLocation().map(locationMapper::map)

    override fun getNearbyMovieTheaters(coords: Location): Single<List<Location>> =
        hereApiService.getNearbyMovieTheaters("${coords.lat},${coords.lon}")
            .map { it.results }
            .map { it.items }
            .map(hereLocationMapper::mapList)
}