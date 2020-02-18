package com.illiarb.tmdbclient.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.discover.DiscoverModel.UiEvent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdbexplorer.coreui.common.Text
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
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
        class ItemClick(val item: Movie) : UiEvent()
        class ApplyFilter(val id: Int) : UiEvent()
        object ClearFilter : UiEvent()
    }
}

class DefaultDiscoverModel @Inject constructor(
    private val genreId: Int,
    private val genresInteractor: GenresInteractor,
    private val moviesInteractor: MoviesInteractor,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), DiscoverModel {

    private val resultsLiveData = MutableLiveData<List<Movie>>()
    private val screenTitleLiveData = MutableLiveData<Text>()
    private val genresLiveData = MutableLiveData<List<Genre>>()
    private val selectedChipLiveData = MutableLiveData<Int>()
    private val isLoadingLiveData = MutableLiveData<Boolean>()

    override val results: LiveData<List<Movie>>
        get() = resultsLiveData

    override val genres: LiveData<List<Genre>>
        get() = genresLiveData

    override val screenTitle: LiveData<Text>
        get() = screenTitleLiveData

    override val selectedChip: LiveData<Int>
        get() = selectedChipLiveData

    override val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    init {
        viewModelScope.launch {
            val genres = genresInteractor.getAllGenres()
            if (genres is Result.Success) {
                genresLiveData.value = genres.data
                applyFilter(genreId)
            }
        }
    }

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ClearFilter -> applyFilter()
            is UiEvent.ApplyFilter -> applyFilter(event.id)
            is UiEvent.ItemClick ->
                router.executeAction(ShowMovieDetails(event.item.id)).also(analyticsService::trackRouterAction)
        }
    }

    private fun applyFilter(id: Int = Genre.GENRE_ALL) = viewModelScope.launch {
        if (selectedChipLiveData.value == id) {
            // do nothing if this genre are already applied
            return@launch
        }

        val genres = genresLiveData.value
        val selected = genres?.find { it.id == id }

        if (selected == null) {
            screenTitleLiveData.value = Text.AsResource(R.string.discover_genres_title)
        } else {
            screenTitleLiveData.value = Text.AsString(selected.name)
            selectedChipLiveData.value = selected.id
        }

        isLoadingLiveData.value = true

        val movies = moviesInteractor.discoverMovies(id)
        if (movies is Result.Success) {
            resultsLiveData.value = movies.data
        }

        isLoadingLiveData.value = false
    }
}