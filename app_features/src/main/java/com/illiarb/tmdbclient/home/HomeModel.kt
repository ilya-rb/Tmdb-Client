package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.home.HomeModel.UiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.analytics.AnalyticEvent.RouterAction
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
        data class ItemClick(val item: Any) : UiEvent()
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
            movieSectionsLiveData.postValue(Async.Loading())
            movieSectionsLiveData.postValue(homeInteractor.getHomeSections().asAsync())

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
            is UiEvent.ItemClick -> {
                val action = when (event.item) {
                    is Movie -> ShowMovieDetails(event.item.id)
                    is Genre -> ShowDiscover(event.item.id)
                    is String -> ShowDiscover()
                    else -> null
                }

                action?.let {
                    analyticsService.trackEvent(RouterAction(it))
                    router.executeAction(it)
                }
            }
            is UiEvent.SettingsClick -> {
                val action = ShowSettings
                analyticsService.trackEvent(RouterAction(action))
                router.executeAction(action)
            }
        }
    }
}