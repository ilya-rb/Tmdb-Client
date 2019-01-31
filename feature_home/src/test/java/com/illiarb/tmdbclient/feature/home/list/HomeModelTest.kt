package com.illiarb.tmdbclient.feature.home.list

import com.illiarb.tmdbclient.feature.home.list.domain.GetAllMovies
import com.illiarb.tmdbclient.feature.home.list.domain.MovieBlock
import com.illiarb.tmdbclient.feature.home.list.presentation.HomeModel
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdbcliient.core_test.runWithSubscription
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureName
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdbexplorer.coreui.navigation.*
import com.illiarb.tmdblcient.core.navigation.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 28.01.19.
 */
@ExperimentalCoroutinesApi
class HomeModelTest {

    private val router = mock<Router>()
    private val authenticator = mock<Authenticator>()

    private val featureConfig = mock<FeatureConfig>()
        .apply {
            Mockito
                .`when`(isFeatureEnabled(FeatureName.AUTH))
                .thenReturn(true)

            Mockito
                .`when`(isFeatureEnabled(FeatureName.SEARCH))
                .thenReturn(false)
        }

    private val getAllMovies = mock<GetAllMovies>()
        .apply {
            runBlocking {
                Mockito
                    .`when`(executeAsync(Unit))
                    .thenReturn(Result.Success(createMovieBlockList(3)))
            }
        }

    private val homeModel = HomeModel(featureConfig, getAllMovies, authenticator, router)

    @Test
    fun `on auth enabled and search disabled proper states are set`() {
        runBlocking {
            verify(getAllMovies).executeAsync(Unit)
            verify(featureConfig).isFeatureEnabled(FeatureName.SEARCH)
            verify(featureConfig).isFeatureEnabled(FeatureName.AUTH)

            runWithSubscription(homeModel.stateObservable()) { observer ->
                observer.withLatest {
                    assertTrue(it.isAuthEnabled)
                    assertFalse(it.isSearchEnabled)
                }
            }
        }
    }

    @Test
    fun `on start movies fetched with progress`() {
        runBlocking {
            verify(getAllMovies).executeAsync(Unit)
            verify(featureConfig).isFeatureEnabled(FeatureName.SEARCH)
            verify(featureConfig).isFeatureEnabled(FeatureName.AUTH)

            runWithSubscription(homeModel.stateObservable()) { observer ->
                observer.withLatest {
                    assertFalse(it.isLoading)
                    assertTrue(it.movies.isNotEmpty())
                }
            }
        }
    }

    @Test
    fun `on search clicked search screen is opened`() {
        homeModel.onSearchClick()

        verify(router).navigateTo(SearchScreen)
    }

    @Test
    fun `on movie clicked details screen is opened`() {
        val movie = FakeEntityFactory.createFakeMovie()

        homeModel.onMovieClick(movie)

        verify(router).navigateTo(MovieDetailsScreen(movie.id))
    }

    @Test
    fun `on account click if not logged in auth screen is opened`() {
        runBlocking {
            Mockito
                .`when`(authenticator.isAuthenticated())
                .thenReturn(false)

            homeModel.onAccountClick()

            verify(router).navigateTo(AuthScreen)
            verify(authenticator).isAuthenticated()
        }
    }

    @Test
    fun `on account click if logged in account screen is opened`() {
        runBlocking {
            Mockito
                .`when`(authenticator.isAuthenticated())
                .thenReturn(true)

            homeModel.onAccountClick()

            verify(router).navigateTo(AccountScreen)
            verify(authenticator).isAuthenticated()
        }
    }

    private fun createMovieBlockList(size: Int) = mutableListOf<MovieBlock>().apply {
        for (i in 0..size) {
            val filter = MovieFilter(
                MovieFilter.TYPE_NOW_PLAYING,
                "now_playing"
            )
            val movies = FakeEntityFactory.createFakeMovieList(size = 3)
            add(MovieBlock(filter, movies))
        }
    }
}