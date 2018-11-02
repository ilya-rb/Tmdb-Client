package com.illiarb.tmdbclient.storage.mappers.hereapi

import com.illiarb.tmdbclient.storage.dto.hereapi.HereLocationDto
import com.illiarb.tmdbclient.storage.mappers.Mapper
import com.illiarb.tmdblcient.core.entity.Location
import javax.inject.Inject

/**
 * @author ilya-rb on 02.11.18.
 */
class HereLocationMapper @Inject constructor() : Mapper<HereLocationDto, Location> {

    override fun map(from: HereLocationDto): Location =
        Location(
            from.position[0].toDouble(),
            from.position[1].toDouble()
        )
}