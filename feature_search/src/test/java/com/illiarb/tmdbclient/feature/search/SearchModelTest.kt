package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbclient.feature.search.domain.SearchMovies
import com.illiarb.tmdbclient.feature.search.presentation.SearchModel
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState
import com.illiarb.tmdbclient.feature.search.presentation.ShowSearchFilters
import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.ScreenData
import com.illiarb.tmdblcient.core.util.observable.Observer
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 21.01.19.
 */
class SearchModelTest {

    private val searchMovies = Mockito.mock(SearchMovies::class.java)
    private val router = object : Router {
        override fun navigateTo(data: ScreenData) {
            // No-op
        }
    }

    private val model = SearchModel(searchMovies, router)

    @Test
    fun `on user clicks clear query, search icon is displayed`() {
        val observer = createTestObserver<SearchUiState> {
            Assert.assertEquals(it.icon, SearchUiState.SearchIcon.Search)
            Assert.assertEquals(it.isSearchRunning, false)
        }

        runWithSubscription(observer) {
            model.onClearClicked()
        }
    }

    @Test
    fun `on user clicks filter show filter action is invoked`() {
        val observer = createTestObserver<UiAction> {
            Assert.assertSame(it, ShowSearchFilters)
        }

        runWithActionSubscription(observer) {
            model.onFilterClicked()
        }
    }

    private inline fun runWithActionSubscription(observer: Observer<UiAction>, block: () -> Unit) {
        model.actionsObservable().addObserver(observer)
        block()
        model.actionsObservable().removeObserver(observer)
    }

    private inline fun runWithSubscription(observer: Observer<SearchUiState>, block: () -> Unit) {
        model.stateObservable().addObserver(observer)
        block()
        model.stateObservable().removeObserver(observer)
    }

    private inline fun <T> createTestObserver(crossinline onNewValue: (T) -> Unit): Observer<T> {
        return object : Observer<T> {
            override fun onNewValue(value: T) {
                onNewValue(value)
            }
        }
    }

}