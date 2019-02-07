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
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

/**
 * @author ilya-rb on 30.01.19.
 */
@ExperimentalCoroutinesApi
object MovieRepositoryTest : Spek({

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
        val type = "now_playing"
        val movies = createMovieModelList(2)

        Mockito
            .`when`(movieService.getMoviesByType(type))
            .thenReturn(CompletableDeferred((ResultsModel(movies))))

        Mockito
            .`when`(storage.storeMovies(type, movies))
            .thenReturn(true)

        describe("a movie repository fetch") {
            context("on type $type") {
                val result = runBlocking { repository.getMoviesByType(type, true) }

                it("should fetch movies from network with type = $type") {
                    assertTrue(result.isNotEmpty())
                    @Suppress("DeferredResultUnused")
                    Mockito.verify(movieService).getMoviesByType(type)
                }

                after {
                    it("Should store fetched movies in cache") {
                        Mockito.verify(storage.storeMovies(type, movies))
                    }
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