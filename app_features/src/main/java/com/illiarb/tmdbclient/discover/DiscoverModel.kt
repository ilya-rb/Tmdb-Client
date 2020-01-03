package com.illiarb.tmdbclient.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DiscoverModel {

    val results: LiveData<List<Movie>>

    val genres: LiveData<List<Genre>>

    val screenTitle: LiveData<String>

    val selectedChip: LiveData<Int>

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
    private val _screenTitle = MutableLiveData<String>()
    private val _genres = MutableLiveData<List<Genre>>()
    private val _selectedChip = MutableLiveData<Int>()

    override val results: LiveData<List<Movie>>
        get() = _results

    override val genres: LiveData<List<Genre>>
        get() = _genres

    override val screenTitle: LiveData<String>
        get() = _screenTitle

    override val selectedChip: LiveData<Int>
        get() = _selectedChip

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Init -> init(event.id)
            is UiEvent.ClearFilter -> applyFilter()

            is UiEvent.ApplyFilter -> {
                val selected = _genres.value?.find { it.id == event.id }
                selected?.let { genre ->
                    _screenTitle.value = genre.name
                    _selectedChip.value = genre.id
                    applyFilter(genre.id)
                }
            }

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
        val movies = if (genreId == Genre.GENRE_ALL) {
            tmdbService.discoverMovies()
        } else {
            tmdbService.discoverMovies(genreId)
        }

        when (movies) {
            is Result.Success -> _results.postValue(movies.data)
            is Result.Error -> {
                // TODO:
            }
        }

        when (val genres = tmdbService.getMovieGenres()) {
            is Result.Success -> _genres.postValue(genres.data)
            is Result.Error -> {
                // TODO:
            }
        }
    }

    private fun applyFilter(id: Int = -1) = viewModelScope.launch {
        when (val result = tmdbService.discoverMovies(id)) {
            is Result.Success -> _results.postValue(result.data)
            is Result.Error -> {
                // TODO:
            }
        }
    }
}