package com.tmdbclient.servicetmdb.internal.repository

import com.tmdbclient.servicetmdb.domain.Genre
import com.illiarb.tmdbclient.tools.DispatcherProvider
import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.internal.network.api.GenreApi
import com.tmdbclient.servicetmdb.internal.cache.TmdbCache
import com.tmdbclient.servicetmdb.internal.network.mappers.GenreMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

internal interface GenresRepository {

  suspend fun getGenres(): Result<List<Genre>>
}

@Singleton
internal class DefaultGenresRepository @Inject constructor(
  private val genreApi: GenreApi,
  private val cache: TmdbCache,
  private val dispatcherProvider: DispatcherProvider,
  private val genreMapper: GenreMapper
) : GenresRepository {

  override suspend fun getGenres(): Result<List<Genre>> = Result.create {
    withContext(dispatcherProvider.io) {
      val cachedGenres = cache.getGenres()
      if (cachedGenres.isNotEmpty()) {
        genreMapper.mapList(cachedGenres)
      } else {
        val genres = genreApi.getGenres().unwrap().genres
        cache.storeGenres(genres)
        genreMapper.mapList(genres)
      }
    }
  }
}