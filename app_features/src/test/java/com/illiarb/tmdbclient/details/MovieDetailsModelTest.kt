package com.illiarb.tmdbclient.details

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
class MovieDetailsModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDetailsViewModel(
            movieId = 123,
            moviesInteractor = TestDependencyProvider.provideMoviesInteractor()
        )
    }

    @Test
    fun `should emit loading an data after it`() = mainCoroutineRule.runBlocking {
        val state = viewModel.movie.getOrAwaitValue()
        assertTrue(state is Async.Loading)

        val data = viewModel.movie.getOrAwaitValue()
        assertTrue(data is Async.Success)
    }
}