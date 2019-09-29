package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    private val featureConfig: FeatureFlagStore,
    private val moviesService: TmdbService,
    private val router: Router
) : BasePresentationModel(), HomeViewModel {

    private val _movieSectionsData = MutableLiveData<Async<List<MovieSection>>>()

    init {
        launch {
            moviesService.getAllMovies()
                .zip(moviesService.getMovieGenres()) { movieBlocks, genres ->
                    val allSections = mutableListOf<MovieSection>()

                    movieBlocks.getOrThrow().forEach { block ->
                        if (block.filter.isNowPlaying()) {
                            allSections.add(block.asMovieSection())
                            allSections.add(GenresSection(genres.getOrThrow()))
                        } else {
                            allSections.add(block.asMovieSection())
                        }
                    }
                    Success(allSections.toList()) as Async<List<MovieSection>>
                }
                .onStart { emit(Loading()) }
                .catch { emit(Fail(it)) }
                .collect { _movieSectionsData.value = it }
        }
    }

    private val _isAccountVisible = MutableLiveData<Boolean>()
        .apply { value = featureConfig.isFeatureEnabled(FeatureFlag.AUTH) }

    override val movieSections: LiveData<Async<List<MovieSection>>>
        get() = _movieSectionsData

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

    private fun MovieBlock.asMovieSection(): MovieSection =
        if (filter.isNowPlaying()) {
            NowPlayingSection(filter.name, movies)
        } else {
            ListSection(filter.name, movies)
        }
}