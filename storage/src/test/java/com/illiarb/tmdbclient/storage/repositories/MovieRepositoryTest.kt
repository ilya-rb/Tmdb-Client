package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.mappers.GenreMapper
import com.illiarb.tmdbclient.storage.mappers.MovieMapper
import com.illiarb.tmdbclient.storage.mappers.PersonMapper
import com.illiarb.tmdbclient.storage.mappers.ReviewMapper
import com.illiarb.tmdbclient.storage.network.api.service.MovieService
import com.illiarb.tmdbclient.storage.network.api.service.SearchService
import com.illiarb.tmdbcliient.core_test.TestDispatcherProvider
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

    private val repository = MoviesRepositoryImpl(
        movieService,
        searchService,
        dispatcherProvider,
        storage,
        MovieMapper(GenreMapper(), PersonMapper(), ReviewMapper()),
        ReviewMapper(),
        resourceResolver
    )
}