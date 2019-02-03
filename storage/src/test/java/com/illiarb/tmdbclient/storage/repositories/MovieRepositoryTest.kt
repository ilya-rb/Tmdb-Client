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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 30.01.19.
 */
@ExperimentalCoroutinesApi
class MovieRepositoryTest {

    private val movieService = mock<MovieService>()
    private val searchService = mock<SearchService>()
    private val dispatcherProvider = TestDispatcherProvider()
    private val storage = mock<PersistableStorage>()
    private val resourceResolver = mock<ResourceResolver>()

    private val movieMapper = MovieMapper(GenreMapper(), PersonMapper(), ReviewMapper())

    private val repository = MoviesRepositoryImpl(
        movieService,
        searchService,
        dispatcherProvider,
        storage,
        movieMapper,
        ReviewMapper(),
        resourceResolver
    )

    @Test
    fun `on refresh movies fetched directly from network and caches`() {
        runBlocking {
            val type = "upcoming"

            Mockito
                .`when`(movieService.getMoviesByType(type))
                .thenReturn(CompletableDeferred(ResultsModel(createMovieModelList(2))))

            repository.getMoviesByType(type, refresh = true)
        }
    }

    @Test
    fun `on fetch movies cached returns first`() {

    }

    private fun createMovieModelList(size: Int): List<MovieModel> {
        return mutableListOf<MovieModel>().apply {
            for (i in 0..size) {
                add(
                    MovieModel(

                    )
                )
            }
        }
    }
}