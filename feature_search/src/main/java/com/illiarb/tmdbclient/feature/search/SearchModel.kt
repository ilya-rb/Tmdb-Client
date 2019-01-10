package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbclient.feature.search.domain.SearchMovies
import com.illiarb.tmdbexplorer.coreui.SimpleStateObservable
import com.illiarb.tmdbexplorer.coreui.StateObservable
import com.illiarb.tmdbexplorer.coreui.base.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class SearchModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val searchMovies: SearchMovies,
    private val router: Router
) : BaseViewModel() {

    private val stateObservable = SimpleStateObservable<SearchState>()
        .apply {
            accept(SearchState.idle())
        }

    override fun provideDefaultDispatcher(): CoroutineDispatcher = dispatcherProvider.mainDispatcher

    fun stateObservable(): StateObservable<SearchState> = stateObservable

    fun search(query: String) = launch(context = coroutineContext) {
        stateObservable.accept(SearchState(true, Collections.emptyList(), null))
        try {
            val results = searchMovies.execute(query)
            stateObservable.accept(SearchState(false, results, null))
        } catch (e: Exception) {
            // Process error
        }
    }

    fun onMovieClicked(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }
}