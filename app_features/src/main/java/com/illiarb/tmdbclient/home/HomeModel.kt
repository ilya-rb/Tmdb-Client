package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.illiarb.tmdbclient.home.HomeModel.UiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.analytics.AnalyticEvent.RouterAction
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowSettings
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Async.Fail
import com.illiarb.tmdblcient.core.util.Async.Loading
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), HomeModel {

    private val movieSectionsLiveData = flow { emit(homeInteractor.getHomeSections()) }
        .map { it.asAsync() }
        .onStart { emit(Loading()) }
        .catch { emit(Fail(it)) }
        .asLiveData()

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