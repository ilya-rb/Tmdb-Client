package com.illiarb.tmdbclient.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.illiarb.tmdbclient.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.illiarb.tmdbcliient.coretest.flow.TestCollector
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
    MovieDetailsViewModel(
      movieId = 123,
      moviesInteractor = TestDependencyProvider.provideMoviesInteractor(),
      router = router,
      analyticsService = TestDependencyProvider.provideAnalyticsService()
    )
  }

  @Test
  fun `should emit loading an data after it`() = mainCoroutineRule.runBlocking {
    val stateCollector = TestCollector<MovieDetailsViewModel.State>()
    stateCollector.test(viewModel.state)

    assertTrue(stateCollector.current().movie is Async.Loading)
    assertTrue(stateCollector.current().movie is Async.Success)
  }

  @Test
  fun `should go to movie details on similar movie click`() {
    val movie = FakeEntityFactory.createFakeMovie()
    viewModel.events.offer(Event.MovieClicked(movie))

    val action = argumentCaptor<Router.Action>()
    verify(router).executeAction(action.capture())
    assertTrue(action.firstValue is Router.Action.ShowMovieDetails)
  }
}