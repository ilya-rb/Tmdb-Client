package com.illiarb.tmdbclient.home

import com.illiarb.tmdbcliient.core_test.TestDependencyProvider
import com.illiarb.tmdbcliient.core_test.viewmodel.BaseViewModelTest
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Loading
import com.illiarb.tmdblcient.core.util.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseViewModelTest() {

    private val homeViewModel: HomeViewModel by lazy {
        HomeModel(
            TestDependencyProvider.provideFeatureFlagStore(),
            TestDependencyProvider.provideTmdbService(),
            TestDependencyProvider.router
        )
    }

    @Test
    fun `it should return false for account feature`() = runBlockingTest {
        homeViewModel.isAccountVisible.observeForever {
            Assert.assertFalse(it)
        }
    }

    @Test
    fun `should post a loading and the content`() {
        val states = mutableListOf<Async<*>>()

        homeViewModel.movieSections.observeForever {
            states.add(it)

            if (states.size == 2) {
                assertTrue(states.first() is Loading<*>)
                assertTrue(states[1] is Success<*>)
            }
        }
    }

    @Test
    fun `should contain 4 sections with now playing first`() {
        homeViewModel.movieSections.observeForever {
            if (it is Success) {
                val sections = it()

                assertEquals(sections.size, 4)
                assertTrue(sections[0] is NowPlayingSection)
            }
        }
    }

    @Test
    fun `should contain genre section after now playing`() {
        homeViewModel.movieSections.observeForever {
            if (it is Success) {
                val sections = it()

                assertTrue(sections[0] is NowPlayingSection)
                assertTrue(sections[1] is GenresSection)
            }
        }
    }
}