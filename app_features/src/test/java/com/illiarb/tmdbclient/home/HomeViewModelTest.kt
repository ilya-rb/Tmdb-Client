package com.illiarb.tmdbclient.home

import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdbcliient.coretest.viewmodel.BaseViewModelTest
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
        homeViewModel.isAccountVisible.observeForever {
            assertFalse(it)
        }
    }

    @Test
    fun `should post a loading and the content`() {
        val states = mutableListOf<Async<*>>()

        homeViewModel.movieSections.observeForever {
            states.add(it)

            if (states.size == 2) {
                assertTrue(states.first() is Async.Loading<*>)
                assertTrue(states[1] is Async.Success<*>)
            }
        }
    }
}