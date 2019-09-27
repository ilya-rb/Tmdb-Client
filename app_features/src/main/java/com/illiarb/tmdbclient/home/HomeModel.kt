package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.illiarb.tmdbclient.home.HomeViewModel.HomeUiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.*
import com.illiarb.tmdblcient.core.feature.flags.FeatureFlag
import com.illiarb.tmdblcient.core.feature.flags.FeatureFlagStore
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    private val featureConfig: FeatureFlagStore,
    private val moviesService: TmdbService,
    private val router: Router
) : BasePresentationModel(), HomeViewModel {

    private val _movieSections = liveData {
        moviesService.getAllMovies()
            .map {
                when (it) {
                    is Uninitialized -> Uninitialized
                    is Loading -> Loading()
                    is Success -> Success(it().asSections())
                    is Fail -> Fail(it.error)
                }
            }
            .collect { emit(it) }
    }

    private val _isAccountVisible = liveData {
        emit(featureConfig.isFeatureEnabled(FeatureFlag.AUTH))
    }

    override val movieSections: LiveData<Async<List<MovieSection>>>
        get() = _movieSections

    override val isAccountVisible: LiveData<Boolean>
        get() = _isAccountVisible

    override fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.ItemClick -> {
                val movie = event.item as Movie
                router.executeAction(ShowMovieDetails(movie.id))
            }
        }
    }

    private fun List<MovieBlock>.asSections(): List<MovieSection> = map { (filter, movies) ->
        if (filter.code == MovieFilter.TYPE_NOW_PLAYING) {
            NowPlayingSection(filter.name, movies)
        } else {
            ListSection(filter.name, movies)
        }
    }
}