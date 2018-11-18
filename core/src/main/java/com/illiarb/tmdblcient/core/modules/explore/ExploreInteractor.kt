package com.illiarb.tmdblcient.core.modules.explore

import com.illiarb.tmdblcient.core.entity.Location
import io.reactivex.Single

/**
 * @author ilya-rb on 02.11.18.
 */
interface ExploreInteractor {

    fun getNearbyMovieTheaters(): Single<List<Location>>
}