package com.illiarb.tmdbclient.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.illiarb.tmdbclient.details.MovieDetailsModel.UiEvent
import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.illiarb.tmdbcliient.coretest.ext.getOrAwaitValue
import com.illiarb.tmdbcliient.coretest.rules.MainCoroutineRule
import com.illiarb.tmdbcliient.coretest.rules.runBlocking
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.util.Async
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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

    private val router = mock<Router>()

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDetailsViewModel(
            movieId = 123,
            moviesInteractor = TestDependencyProvider.provideMoviesInteractor(),
            router = router,
            analyticsService = TestDependencyProvider.provideAnalyticsService()
        )
    }

    @Test
    fun `should emit loading an data after it`() = mainCoroutineRule.runBlocking {
        val state = viewModel.movie.getOrAwaitValue()
        assertTrue(state is Async.Loading)

        val data = viewModel.movie.getOrAwaitValue()
        assertTrue(data is Async.Success)
    }

    @Test
    fun `should go to movie details on similar movie click`() {
        val movie = FakeEntityFactory.createFakeMovie()
        viewModel.onUiEvent(UiEvent.ItemClick(movie))

        val action = argumentCaptor<Router.Action>()
        verify(router).executeAction(action.capture())

        assertTrue(action.firstValue is Router.Action.ShowMovieDetails)
    }
}