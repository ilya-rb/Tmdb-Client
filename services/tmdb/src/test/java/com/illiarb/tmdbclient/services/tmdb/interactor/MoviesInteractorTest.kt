package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.libs.test.tools.TestDispatcherProvider
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.configuration.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultMoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.DiscoverApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.PersonMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.ReviewMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.ResultsModel
import com.illiarb.tmdbclient.services.tmdb.repository.TestMovieRepository
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