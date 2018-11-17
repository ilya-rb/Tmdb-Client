package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdblcient.core.entity.Location
import javax.inject.Inject
import android.location.Location as AndroidLocation

/**
 * @author ilya-rb on 01.11.18.
 */
class LocationMapper @Inject constructor() : Mapper<AndroidLocation, Location> {
    override fun map(from: AndroidLocation): Location = Location(from.latitude, from.longitude)
}