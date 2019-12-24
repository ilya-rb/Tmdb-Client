package com.tmdbclient.service_tmdb.configuration

import com.tmdbclient.service_tmdb.BuildConfig
import com.tmdbclient.service_tmdb.cache.TmdbCache
import javax.inject.Inject

class ImageUrlCreator @Inject constructor(private val cache: TmdbCache) {

    fun createImageUrl(path: String?): String {
        val configuration = cache.getConfiguration()
        val baseUrl = configuration.images.baseUrl

        return if (baseUrl.isBlank()) {
            "${BuildConfig.IMG_URL}/$path"
        } else {
            "$baseUrl/$path"
        }
    }
}