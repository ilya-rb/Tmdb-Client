package com.illiarb.tmdbclient.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.discover.DiscoverModel.UiEvent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdbexplorer.coreui.common.Text
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent.RouterAction
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DiscoverModel {

    val results: LiveData<List<Movie>>

    val genres: LiveData<List<Genre>>

    val screenTitle: LiveData<Text>

    val selectedChip: LiveData<Int>

    val isLoading: LiveData<Boolean>

    fun onUiEvent(event: UiEvent)

    sealed class UiEvent {
        class ItemClick(val item: Any) : UiEvent()
        class ApplyFilter(val id: Int) : UiEvent()
        class Init(val id: Int) : UiEvent()
        object ClearFilter : UiEvent()
    }
}

class DefaultDiscoverModel @Inject constructor(
    private val tmdbService: TmdbService,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), DiscoverModel {

    private val _results = MutableLiveData<List<Movie>>()
    private val _screenTitle = MutableLiveData<Text>()
    private val _genres = MutableLiveData<List<Genre>>()
    private val _selectedChip = MutableLiveData<Int>()
    private val _isLoading = MutableLiveData<Boolean>()

    override val results: LiveData<List<Movie>>
        get() = _results

    override val genres: LiveData<List<Genre>>
        get() = _genres

    override val screenTitle: LiveData<Text>
        get() = _screenTitle

    override val selectedChip: LiveData<Int>
        get() = _selectedChip

    override val isLoading: LiveData<Boolean>
        get() = _isLoading

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Init -> init(event.id)
            is UiEvent.ClearFilter -> applyFilter()
            is UiEvent.ApplyFilter -> applyFilter(event.id)
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
        val genres = tmdbService.getMovieGenres()
        if (genres is Result.Success) {
            _genres.value = genres.data
            applyFilter(genreId)
        }
    }

    private fun applyFilter(id: Int = Genre.GENRE_ALL) = viewModelScope.launch {
        if (_selectedChip.value == id) {
            // do nothing if this genre are already applied
            return@launch
        }

        val genres = _genres.value
        val selected = genres?.find { it.id == id }

        if (selected == null) {
            _screenTitle.value = Text.AsResource(R.string.discover_genres_title)
        } else {
            _screenTitle.value = Text.AsString(selected.name)
            _selectedChip.value = selected.id
        }

        val movies = tmdbService.discoverMovies(id)
        if (movies is Result.Success) {
            _results.value = movies.data
        }
    }
}