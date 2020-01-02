package com.illiarb.tmdbclient.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.discover.DiscoverModel.UiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent.RouterAction
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

interface DiscoverModel {

    val results: LiveData<List<Movie>>

    val genres: LiveData<List<Genre>>

    fun onUiEvent(event: UiEvent)

    sealed class UiEvent {
        class ItemClick(val item: Any) : UiEvent()
        class ApplyFilter(val id: Int) : UiEvent()
        class Init(val genreId: Int) : UiEvent()
        object ClearFilter : UiEvent()
    }
}

class DefaultDiscoverModel @Inject constructor(
    private val tmdbService: TmdbService,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), DiscoverModel {

    private val resultsChannel = Channel<List<Movie>>()
    private val _results = resultsChannel.consumeAsFlow()
        .catch { emit(Collections.emptyList()) }
        .asLiveData()

    private val _genres = flow { emit(tmdbService.getMovieGenres().getOrThrow()) }
        .catch { emit(Collections.emptyList()) }
        .asLiveData()

    override val results: LiveData<List<Movie>>
        get() = _results

    override val genres: LiveData<List<Genre>>
        get() = _genres

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Init -> init(event.genreId)
            is UiEvent.ApplyFilter -> applyFilter(event.id)
            is UiEvent.ClearFilter -> applyFilter()
            is UiEvent.ItemClick -> {
                if (event.item is Movie) {
                    val action = ShowMovieDetails(event.item.id)
                    analyticsService.trackEvent(RouterAction(action))
                    router.executeAction(action)
                }
            }
        }
    }

    private fun init(genreId: Int) = viewModelScope.launch {
        val result = if (genreId == Genre.GENRE_ALL) {
            tmdbService.discoverMovies()
        } else {
            tmdbService.discoverMovies(genreId)
        }

        when (result) {
            is Result.Success -> resultsChannel.offer(result.data)
        }
    }

    private fun applyFilter(id: Int = -1) = viewModelScope.launch {
        when (val result = tmdbService.discoverMovies(id)) {
            is Result.Success -> resultsChannel.offer(result.data)
        }
    }
}