package com.illiarb.tmdbclient.storage.mappers.here

import com.illiarb.tmdbclient.storage.mappers.Mapper
import com.illiarb.tmdbclient.storage.model.here.LocationModel
import com.illiarb.tmdblcient.core.entity.Location
import javax.inject.Inject

/**
 * @author ilya-rb on 02.11.18.
 */
class HereLocationMapper @Inject constructor() : Mapper<LocationModel, Location> {
    override fun map(from: LocationModel): Location =
        Location(
            from.title,
            from.position[0],
            from.position[1],
            from.distance
        )
}