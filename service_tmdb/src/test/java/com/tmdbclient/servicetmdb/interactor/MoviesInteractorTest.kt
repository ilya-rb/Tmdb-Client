package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Result
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.api.MovieApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.image.ImageUrlCreator
import com.tmdbclient.servicetmdb.mappers.GenreMapper
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.mappers.PersonMapper
import com.tmdbclient.servicetmdb.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.model.ResultsModel
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MoviesInteractorTest {

  private val cache = mock<TmdbCache>()
  private val discoverApi = mock<DiscoverApi>()
  private val movieApi = mock<MovieApi>()
  private val moviesRepository = TestDependencyProvider.provideMovieRepository()

  private val interactor = DefaultMoviesInteractor(
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
    TestDependencyProvider.provideDispatcherProvider()
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