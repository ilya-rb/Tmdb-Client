package com.illiarb.tmdbclient.storage.mappers

import android.location.Location
import javax.inject.Inject

/**
 * @author ilya-rb on 01.11.18.
 */
class LocationMapper @Inject constructor() : Mapper<Location, com.illiarb.tmdblcient.core.entity.Location> {

    override fun map(from: Location): com.illiarb.tmdblcient.core.entity.Location =
        com.illiarb.tmdblcient.core.entity.Location(from.latitude, from.longitude)
}