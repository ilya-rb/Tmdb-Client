package com.illiarb.tmdbclient.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.discover.DiscoverModel.UiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.flow.catch
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
        class FiltersPanelClosed(val id: Int) : UiEvent()
    }
}

class DefaultDiscoverModel @Inject constructor(
    private val tmdbService: TmdbService,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), DiscoverModel {

    private val _results = flow { emit(tmdbService.discoverMovies().getOrThrow()) }
        .catch { emit(Collections.emptyList()) }
        .asLiveData() as MutableLiveData

    private val _genres = flow { emit(tmdbService.getMovieGenres().getOrThrow()) }
        .catch { emit(Collections.emptyList()) }
        .asLiveData()

    override val results: LiveData<List<Movie>>
        get() = _results

    override val genres: LiveData<List<Genre>>
        get() = _genres

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.FiltersPanelClosed -> applyFilter(event.id)
            is UiEvent.ItemClick -> {
                if (event.item is Movie) {
                    val action = Router.Action.ShowMovieDetails(event.item.id)
                    analyticsService.trackEvent(AnalyticEvent.RouterAction(action))
                    router.executeAction(action)
                }
            }
        }
    }

    private fun applyFilter(id: Int) = viewModelScope.launch {
        when (val result = tmdbService.discoverMovies(id)) {
            is Result.Success -> {
                _results.value = result.data
            }
        }
    }
}