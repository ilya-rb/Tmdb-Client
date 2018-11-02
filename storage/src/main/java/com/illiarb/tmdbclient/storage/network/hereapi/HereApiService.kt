package com.illiarb.tmdbclient.storage.network.hereapi

import com.illiarb.tmdbclient.storage.dto.hereapi.HereLocationDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author ilya-rb on 02.11.18.
 */
interface HereApiService {

    companion object {
        const val CATEGORY_CINEMA = "cinema"
        const val CATEGORY_SYSTEM = "places"
        const val DEFAULT_RADIUS = 5
    }

    @GET("discover/explore")
    fun getNearbyMovieTheaters(
        @Query("at") coordinates: String,
        @Query("in") radius: Int = DEFAULT_RADIUS,
        @Query("cs") systemCategory: String = CATEGORY_SYSTEM,
        @Query("cat") category: String = CATEGORY_CINEMA
    ): Single<List<HereLocationDto>>
}