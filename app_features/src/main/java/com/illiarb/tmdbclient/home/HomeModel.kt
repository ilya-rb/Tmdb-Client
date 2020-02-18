package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.home.HomeModel.UiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowSettings
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

interface HomeModel {

    val movieSections: LiveData<Async<List<MovieSection>>>

    fun onUiEvent(event: UiEvent)

    sealed class UiEvent {
        data class MovieClick(val movie: Movie) : UiEvent()
        data class SeeAllClick(val code: String) : UiEvent()
        data class GenreClick(val genre: Genre) : UiEvent()
        object SettingsClick : UiEvent()
    }
}

class DefaultHomeModel @Inject constructor(
    private val homeInteractor: HomeInteractor,
    private val trendingInteractor: TrendingInteractor,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), HomeModel {

    private val movieSectionsLiveData = MutableLiveData<Async<List<MovieSection>>>()

    init {
        viewModelScope.launch {
            movieSectionsLiveData.value = Async.Loading()
            movieSectionsLiveData.value = homeInteractor.getHomeSections().asAsync()

            val trending = trendingInteractor.getTrending()
            if (trending is Result.Success) {
                val movieSections = movieSectionsLiveData.value
                if (movieSections is Async.Success) {
                    val sectionsList = movieSections().toMutableList()
                    if (sectionsList.isEmpty()) {
                        sectionsList.add(TrendingSection(trending.data))
                    } else {
                        sectionsList.add(1, TrendingSection(trending.data))
                    }
                    movieSectionsLiveData.value = Async.Success(sectionsList)
                }
            }
        }
    }

    override val movieSections: LiveData<Async<List<MovieSection>>>
        get() = movieSectionsLiveData

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.SeeAllClick -> router.executeAction(ShowDiscover())
                .also(analyticsService::trackRouterAction)

            is UiEvent.SettingsClick -> router.executeAction(ShowSettings)
                .also(analyticsService::trackRouterAction)

            is UiEvent.MovieClick ->
                router.executeAction(ShowMovieDetails(event.movie.id))
                    .also(analyticsService::trackRouterAction)

            is UiEvent.GenreClick ->
                router.executeAction(ShowDiscover(event.genre.id))
                    .also(analyticsService::trackRouterAction)
        }
    }
}