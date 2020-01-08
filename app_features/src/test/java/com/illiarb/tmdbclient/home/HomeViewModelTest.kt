package com.illiarb.tmdbclient.home

import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdbcliient.coretest.ext.getOrAwaitValue
import com.illiarb.tmdbcliient.coretest.viewmodel.BaseViewModelTest
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseViewModelTest() {

    private val homeViewModel: HomeModel by lazy {
        DefaultHomeModel(
            TestDependencyProvider.provideFeatureFlagStore(),
            TestDependencyProvider.provideHomeInteractor(),
            TestDependencyProvider.router,
            TestDependencyProvider.provideAnalyticsService()
        )
    }

    @Test
    fun `it should return false for account feature`() = runBlockingTest {
        val isAccountVisible = homeViewModel.isAccountVisible.getOrAwaitValue()
        assertFalse(isAccountVisible)
    }

    @Test
    fun `should post a loading and the content`() = runBlockingTest {
        val state = homeViewModel.movieSections.getOrAwaitValue()
        assertTrue(state is Async.Loading)

        val data = homeViewModel.movieSections.getOrAwaitValue()
        assertTrue(data is Async.Success)
    }
}