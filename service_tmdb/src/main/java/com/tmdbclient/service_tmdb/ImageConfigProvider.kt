package com.tmdbclient.service_tmdb

import com.illiarb.tmdblcient.core.storage.WorkManager
import com.tmdbclient.service_tmdb.cache.TmdbCache
import javax.inject.Inject

interface ImageConfigProvider {

    fun getBaseImageUrl(): String

    class TmdbImageConfigProvider @Inject constructor(
        private val cache: TmdbCache,
        private val workManager: WorkManager
    ) : ImageConfigProvider {

        override fun getBaseImageUrl(): String {
            return ""
        }
    }
}