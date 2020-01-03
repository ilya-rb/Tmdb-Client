package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.illiarb.tmdbclient.home.HomeViewModel.HomeUiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.ListSection
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore.FeatureFlag
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent.RouterAction
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    featureConfig: FeatureFlagStore,
    private val moviesService: TmdbService,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), HomeViewModel {

    companion object {
        // Max genres displayed in the section
        private const val GENRES_MAX_SIZE = 8
    }

    private val _movieSectionsData = flow { emit(moviesService.getAllMovies()) }
        .map {
            val genres = moviesService.getMovieGenres()
            val trending = moviesService.getTrending()
            createSections(it.getOrThrow(), genres.getOrThrow(), trending.getOrThrow())
        }
        .map { Async.Success(it) as Async<List<MovieSection>> }
        .onStart { emit(Async.Loading()) }
        .catch { emit(Async.Fail(it)) }
        .asLiveData()

    private val _isAccountVisible =
        MutableLiveData<Boolean>(featureConfig.isFeatureEnabled(FeatureFlag.AUTH))

    override val movieSections: LiveData<Async<List<MovieSection>>>
        get() = _movieSectionsData

    override val isAccountVisible: LiveData<Boolean>
        get() = _isAccountVisible

    override fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.ItemClick -> {
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

    private fun createSections(
        movieBlocks: List<MovieBlock>,
        genres: List<Genre>,
        trending: TrendingSection
    ): List<MovieSection> {
        val result = mutableListOf<MovieSection>()
        val blocksWithMovies = movieBlocks.filter { it.movies.isNotEmpty() }

        blocksWithMovies.forEach { block ->
            // Now playing section should always
            // be on top and genres section immediately after it
            if (block.filter.isNowPlaying()) {
                result.add(0, NowPlayingSection(block.filter.name, block.movies))

                if (genres.isNotEmpty()) {
                    if (genres.size > GENRES_MAX_SIZE) {
                        result.add(1, GenresSection(genres.subList(0, GENRES_MAX_SIZE)))
                    } else {
                        result.add(1, GenresSection(genres))
                    }
                }
            } else {
                result.add(ListSection(block.filter.code, block.filter.name, block.movies))
            }
        }

        if (trending.items.isNotEmpty()) {
            result.add(1, trending)
        }

        return result
    }
}
