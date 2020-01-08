package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.illiarb.tmdbclient.home.HomeModel.UiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore.FeatureFlag
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.analytics.AnalyticEvent.RouterAction
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Async.Fail
import com.illiarb.tmdblcient.core.util.Async.Loading
import com.illiarb.tmdblcient.core.util.Async.Success
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

interface HomeModel {

    val movieSections: LiveData<Async<List<MovieSection>>>

    val isAccountVisible: LiveData<Boolean>

    fun onUiEvent(event: UiEvent)

    sealed class UiEvent {
        data class ItemClick(val item: Any) : UiEvent()
    }
}

class DefaultHomeModel @Inject constructor(
    featureConfig: FeatureFlagStore,
    private val homeInteractor: HomeInteractor,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), HomeModel {

    private val _movieSectionsData = flow { emit(homeInteractor.getHomeSections().getOrThrow()) }
        .map { Success(it) as Async<List<MovieSection>> }
        .onStart { emit(Loading()) }
        .catch { emit(Fail(it)) }
        .asLiveData()

    private val _isAccountVisible =
        MutableLiveData<Boolean>(featureConfig.isFeatureEnabled(FeatureFlag.AUTH))

    override val movieSections: LiveData<Async<List<MovieSection>>>
        get() = _movieSectionsData

    override val isAccountVisible: LiveData<Boolean>
        get() = _isAccountVisible

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
        }
    }
}