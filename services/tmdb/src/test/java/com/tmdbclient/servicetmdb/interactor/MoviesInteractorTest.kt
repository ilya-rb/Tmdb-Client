package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdbclient.util.Result
import com.illiarb.tmdbcliient.coretest.tools.TestDispatcherProvider
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.internal.cache.TmdbCache
import com.tmdbclient.servicetmdb.internal.configuration.Configuration
import com.tmdbclient.servicetmdb.internal.image.ImageUrlCreator
import com.tmdbclient.servicetmdb.internal.interactor.DefaultMoviesInteractor
import com.tmdbclient.servicetmdb.internal.network.api.DiscoverApi
import com.tmdbclient.servicetmdb.internal.network.api.MovieApi
import com.tmdbclient.servicetmdb.internal.network.mappers.GenreMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.MovieMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.PersonMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.internal.network.model.ResultsModel
import com.tmdbclient.servicetmdb.repository.TestMovieRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MoviesInteractorTest {

  private val cache = mock<TmdbCache>()
  private val discoverApi = mock<DiscoverApi>()
  private val movieApi = mock<MovieApi>()
  private val moviesRepository = TestMovieRepository()

  private val interactor =
    DefaultMoviesInteractor(
      moviesRepository,
      discoverApi,
      movieApi,
      MovieMapper(
        GenreMapper(),
        PersonMapper(),
        ReviewMapper(),
        ImageUrlCreator()
      ),
      cache,
      TestDispatcherProvider()
    )

  @Test
  fun `should not pass genre id if all genres are selected`() = runBlockingTest {
    val configuration = Configuration(changeKeys = listOf("images"))

    whenever(cache.getConfiguration()).thenReturn(configuration)
    whenever(discoverApi.discoverMovies(anyOrNull())).thenReturn(Result.Ok(ResultsModel(emptyList())))

    interactor.discoverMovies(Genre.GENRE_ALL)
    verify(discoverApi).discoverMovies(null)
  }

  @Test
  fun `should include images to response if change key is present`() = runBlockingTest {
    val id = 1
    val configuration = Configuration(changeKeys = listOf("images"))

    whenever(cache.getConfiguration()).thenReturn(configuration)

    val details = interactor.getMovieDetails(id).unwrap()
    assertTrue(details.images.isNotEmpty())
  }
}