package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbclient.feature.search.presentation.SearchModel
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState
import com.illiarb.tmdbcliient.core_test.TestObserver
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.SearchInteractor
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

/**
 * 1. Test on query clear correct icon is displayed
 * 2. Test on running query loading is displayed
 * 3. Test on success result loading is not displayed and correct result passed
 * 4. Test on movie click navigates to correct route
 *
 * @author ilya-rb on 21.01.19.
 */
@ExperimentalCoroutinesApi
class SearchModelTest {

    private val mockRouter = mock<Router>()

    private val mockSearchInteractor = mock<SearchInteractor>()

    private val searchModel = SearchModel(mockSearchInteractor, mockRouter)

    private val testStateObserver = TestObserver<SearchUiState>()

    @Before
    fun beforeEachTest() {
        searchModel.stateObservable().addObserver(testStateObserver)
    }

    @After
    fun afterEachTest() {
        testStateObserver.clearValues()
        searchModel.stateObservable().removeObserver(testStateObserver)
    }

    @Test
    fun `on user clicks clear query, search icon is displayed`() {
        // Clear search query from edit text
        searchModel.onClearClicked()

        testStateObserver
            // Idle + new state
            .assertValuesCount(2)
            .withLatest {
                assertEquals(SearchUiState.SearchIcon.Search, it.icon)
                assertEquals(false, it.isSearchRunning)
            }
    }

    @Test
    fun `on search query running progress is displayed and cross icon is displayed`() {
        runBlocking {
            val searchQuery = "test"

            Mockito
                .`when`(mockSearchInteractor.searchMovies(searchQuery))
                .thenReturn(Result.Success(Collections.emptyList()))

            searchModel.search(searchQuery)

            testStateObserver
                // Idle + Loading State + Result state
                .assertValuesCount(3)
                .withPrevious {
                    assertEquals(true, it.isSearchRunning)
                }
                .withLatest {
                    assertEquals(SearchUiState.SearchIcon.Cross, it.icon)
                    assertEquals(false, it.isSearchRunning)
                    assertEquals(SearchUiState.SearchResult.Empty, it.result)
                }
        }
    }

    @Test
    fun `on non-empty result progress is not displayed and result is success`() {
        runBlocking {
            val searchQuery = "test"
            val result = FakeEntityFactory.createFakeMovieList(size = 2)

            Mockito
                .`when`(mockSearchInteractor.searchMovies(searchQuery))
                .thenReturn(Result.Success(result))

            searchModel.search(searchQuery)

            testStateObserver
                // Idle + Loading State + Result state
                .assertValuesCount(3)
                .withPrevious {
                    assertEquals(true, it.isSearchRunning)
                }
                .withLatest {
                    assertEquals(SearchUiState.SearchIcon.Cross, it.icon)
                    assertEquals(false, it.isSearchRunning)
                    assertSame(
                        SearchUiState.SearchResult.Success::class.java,
                        it.result::class.java
                    )

                    val actual = (it.result as SearchUiState.SearchResult.Success).movies
                    assertArrayEquals(result.toTypedArray(), actual.toTypedArray())
                }
        }
    }

    @Test
    fun `on movie clicked navigate to movie details is called`() {
        val movie = FakeEntityFactory.createFakeMovie()

        searchModel.onMovieClicked(movie)

        verify(mockRouter).navigateTo(MovieDetailsScreen(movie.id))
    }
}