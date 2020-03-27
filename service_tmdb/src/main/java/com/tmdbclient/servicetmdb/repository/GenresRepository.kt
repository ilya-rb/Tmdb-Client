package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.GenreApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.mappers.GenreMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface GenresRepository {

  suspend fun getGenres(): Result<List<Genre>>
}

@Singleton
class DefaultGenresRepository @Inject constructor(
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