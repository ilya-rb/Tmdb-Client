package com.tmdbclient.servicetmdb

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.tmdbclient.servicetmdb.api.GenreApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.mappers.GenreMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface GenresRepository {

    suspend fun getGenres(): List<Genre>
}

@Singleton
class DefaultGenresRepository @Inject constructor(
    private val genreApi: GenreApi,
    private val cache: TmdbCache,
    private val dispatcherProvider: DispatcherProvider,
    private val genreMapper: GenreMapper
) : GenresRepository {

    override suspend fun getGenres(): List<Genre> = withContext(dispatcherProvider.io) {
        val cachedGenres = cache.getGenres()
        if (cachedGenres.isNotEmpty()) {
            genreMapper.mapList(cachedGenres)
        } else {
            val genres = genreApi.getGenresAsync().await().genres
            cache.storeGenres(genres)
            genreMapper.mapList(genres)
        }
    }
}