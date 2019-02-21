package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.mappers.GenreMapper
import com.illiarb.tmdbclient.storage.mappers.MovieMapper
import com.illiarb.tmdbclient.storage.mappers.PersonMapper
import com.illiarb.tmdbclient.storage.mappers.ReviewMapper
import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.model.ResultsModel
import com.illiarb.tmdbclient.storage.network.api.service.MovieService
import com.illiarb.tmdbclient.storage.network.api.service.SearchService
import com.illiarb.tmdbcliient.core_test.TestDispatcherProvider
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

/**
 * TODO: Rewrite other tests to Spek2
 *
 * @author ilya-rb on 30.01.19.
 */
@ExperimentalCoroutinesApi
object MovieRepositorySpek : Spek({

    val movieService = mock<MovieService>()
    val searchService = mock<SearchService>()
    val dispatcherProvider = TestDispatcherProvider()
    val storage = mock<PersistableStorage>()
    val resourceResolver = mock<ResourceResolver>()

    val movieMapper = MovieMapper(GenreMapper(), PersonMapper(), ReviewMapper())

    val repository = MoviesRepositoryImpl(
        movieService,
        searchService,
        dispatcherProvider,
        storage,
        movieMapper,
        ReviewMapper(),
        resourceResolver
    )

    group("Movies fetching tests") {
        describe("Movie repository fetching process") {
            val types = arrayOf("now_playing, upcoming, popular, top_rated")

            types.forEach { type ->
                context("on type received = $type") {
                    val movies = createMovieModelList(2)

                    Mockito
                        .`when`(movieService.getMoviesByTypeAsync(type))
                        .thenReturn(CompletableDeferred((ResultsModel(movies))))

                    Mockito
                        .`when`(storage.storeMovies(type, movies))
                        .thenReturn(true)

                    val result = runBlocking { repository.getMoviesByType(type, true) }

                    it("should fetch movies from network with type = $type") {
                        assertTrue(result.isNotEmpty())
                        @Suppress("DeferredResultUnused")
                        Mockito.verify(movieService).getMoviesByTypeAsync(type)
                    }

                    it("Should store fetched movies in cache") {
                        Mockito.verify(storage).storeMovies(type, movies)
                    }
                }
            }
        }

        describe("Movie details fetching") {
            val movie = FakeEntityFactory.createFakeMovie()
            val appendToResponse = "images,reviews"

            context("On given movie id = ${movie.id}") {
                Mockito
                    .`when`(movieService.getMovieDetailsAsync(movie.id, appendToResponse))
                    .thenReturn(CompletableDeferred(MovieModel(id = movie.id)))

                runBlocking { repository.getMovieDetails(movie.id, appendToResponse) }

                it("Should fetch movie details from api") {
                    @Suppress("DeferredResultUnused")
                    verify(movieService).getMovieDetailsAsync(movie.id, appendToResponse)
                    verifyZeroInteractions(storage)
                }
            }
        }
    }
})

fun createMovieModelList(size: Int): List<MovieModel> {
    return mutableListOf<MovieModel>().apply {
        for (i in 0 until size) {
            add(MovieModel())
        }
    }
}