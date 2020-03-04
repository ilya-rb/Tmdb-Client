package com.illiarb.tmdbclient.discover

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.illiarb.tmdbcliient.coretest.flow.test
import com.illiarb.tmdbcliient.coretest.rules.MainCoroutineRule
import com.illiarb.tmdbcliient.coretest.rules.runBlocking
import com.illiarb.tmdbexplorer.coreui.common.Text
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DiscoverViewModelTest {

  @get:Rule
  val instantTaskRule = InstantTaskExecutorRule()

  @get:Rule
  val mainCoroutineRule = MainCoroutineRule()

  private val router = mock<Router>()

  @Test
  fun `should fetch genres on startup`() = mainCoroutineRule.runBlocking {
    val vm = createViewModel()
    val collector = vm.state.test().also { it.collect() }
    assertTrue(collector.current().genres.isNotEmpty())
  }

  @Test
  fun `should apply selected genre on startup`() = mainCoroutineRule.runBlocking {
    val id = 1
    val vm = createViewModel(id)
    val collector = vm.state.test().also { it.collect() }
    val selected = collector.current().selectedGenreId
    assertEquals(id, selected)
  }

  @Test
  fun `should fetch movies from selected genre on startup`() = mainCoroutineRule.runBlocking {
    val genre = FakeEntityFactory.createGenre()
    val vm = createViewModel(genreId = genre.id)
    vm.assertMoviesFromGenre(genre.id)
  }

  @Test
  fun `should set title of selected genre on startup`() = mainCoroutineRule.runBlocking {
    val genre = FakeEntityFactory.createGenre()
    val vm = createViewModel(genreId = genre.id)
    vm.assertScreenTitleIs(genre.name)
  }

  @Test
  fun `should set defaults on no genre is selected on startup`() = mainCoroutineRule.runBlocking {
    val vm = createViewModel()

    val screenTitle = vm.screenTitle.getOrAwaitValue()
    assertTrue(screenTitle is Text.AsResource && screenTitle.id == R.string.discover_genres_title)
  }

  @Test
  fun `should apply filter on some is selected`() = mainCoroutineRule.runBlocking {
    val genre = FakeEntityFactory.createGenre()
    val vm = createViewModel()

    vm.onUiEvent(DiscoverModel.UiEvent.ApplyFilter(genre.id))

    vm.assertScreenTitleIs(genre.name)
    vm.assertMoviesFromGenre(genre.id)
    vm.assertSelectedChipEquals(genre.id)
  }

  @Test
  fun `should open movie details on item click`() = mainCoroutineRule.runBlocking {
    val movie = FakeEntityFactory.createFakeMovie()
    val vm = createViewModel()

    vm.onUiEvent(DiscoverModel.UiEvent.ItemClick(movie))

    val action = argumentCaptor<Router.Action>()
    verify(router).executeAction(action.capture())

    assertTrue(action.firstValue is ShowMovieDetails)
  }

  private fun DiscoverModel.assertScreenTitleIs(title: String) {
    val screenTitle = screenTitle.getOrAwaitValue()
    assertTrue(screenTitle is Text.AsString && screenTitle.text == title)
  }

  private fun DiscoverModel.assertSelectedChipEquals(id: Int) {
    assertEquals(selectedChip.getOrAwaitValue(), id)
  }

  private fun DiscoverModel.assertMoviesFromGenre(id: Int) {
    val movies = results.getOrAwaitValue()
    movies.forEach {
      assertTrue(it.genres.map { genre -> genre.id }.contains(id))
    }
  }

  private fun createViewModel(genreId: Int = Genre.GENRE_ALL): DiscoverViewModel {
    return DiscoverViewModel(
      genreId,
      router,
      TestDependencyProvider.provideMoviesInteractor(),
      TestDependencyProvider.provideGenresInteractor(),
      TestDependencyProvider.provideAnalyticsService()
    )
  }
}