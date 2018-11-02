package com.illiarb.tmdblcient.core.modules.location

import com.illiarb.tmdblcient.core.entity.Location
import io.reactivex.Single

/**
 * @author ilya-rb on 02.11.18.
 */
interface LocationInteractor {

    fun getNearbyMovieTheaters(coords: Location): Single<List<Location>>
}