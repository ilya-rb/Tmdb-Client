package com.illiarb.tmdbclient.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.Collections
import javax.inject.Inject

interface DiscoverModel {

    val results: LiveData<List<Movie>>

    val genres: LiveData<List<Genre>>

    fun onUiEvent(event: UiEvent)

    sealed class UiEvent {
        class ItemClick(val item: Any) : UiEvent()
        class GenreSelected(val id: Int) : UiEvent()
        object FiltersPanelClosed
    }
}

class DefaultDiscoverModel @Inject constructor(
    private val tmdbService: TmdbService,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), DiscoverModel {

    private val _results = flow { emit(tmdbService.discoverMovies().getOrThrow()) }
        .catch { emit(Collections.emptyList()) }
        .asLiveData()

    private val _genres = flow { emit(tmdbService.getMovieGenres().getOrThrow()) }
        .catch { emit(Collections.emptyList()) }
        .asLiveData()

    override val results: LiveData<List<Movie>>
        get() = _results

    override val genres: LiveData<List<Genre>>
        get() = _genres

    override fun onUiEvent(event: DiscoverModel.UiEvent) {
        when (event) {
            is DiscoverModel.UiEvent.ItemClick -> {
                if (event.item is Movie) {
                    val action = Router.Action.ShowMovieDetails(event.item.id)
                    analyticsService.trackEvent(AnalyticEvent.RouterAction(action))
                    router.executeAction(action)
                }
            }
        }
    }
}