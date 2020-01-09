package com.illiarb.tmdbclient.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdbcliient.coretest.ext.getOrAwaitValue
import com.illiarb.tmdbcliient.coretest.rules.MainCoroutineRule
import com.illiarb.tmdbcliient.coretest.rules.runBlocking
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val homeViewModel: HomeModel by lazy {
        DefaultHomeModel(
            TestDependencyProvider.provideHomeInteractor(),
            TestDependencyProvider.router,
            TestDependencyProvider.provideAnalyticsService()
        )
    }

    @Test
    fun `should post a loading and the content`() = mainCoroutineRule.runBlocking {
        val state = homeViewModel.movieSections.getOrAwaitValue()
        assertTrue(state is Async.Loading)

        val data = homeViewModel.movieSections.getOrAwaitValue()
        assertTrue(data is Async.Success)
    }
}