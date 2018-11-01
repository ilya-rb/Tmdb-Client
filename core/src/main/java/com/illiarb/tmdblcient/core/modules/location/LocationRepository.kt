package com.illiarb.tmdblcient.core.modules.location

import com.illiarb.tmdblcient.core.entity.Location
import io.reactivex.Single

/**
 * @author ilya-rb on 01.11.18.
 */
interface LocationRepository {

    fun getCurrentLocation(): Single<Location>

    fun getNearbyMovieTheaters(): Single<List<Location>>

}