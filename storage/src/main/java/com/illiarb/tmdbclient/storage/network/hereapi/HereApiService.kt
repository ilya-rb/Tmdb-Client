package com.illiarb.tmdbclient.storage.network.hereapi

import com.illiarb.tmdbclient.storage.dto.hereapi.HereResults
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
    }

    @GET("discover/explore")
    fun getNearbyMovieTheaters(
        @Query("at") coordinates: String,
        @Query("cs") systemCategory: String = CATEGORY_SYSTEM,
        @Query("cat") category: String = CATEGORY_CINEMA
    ): Single<HereResults>
}