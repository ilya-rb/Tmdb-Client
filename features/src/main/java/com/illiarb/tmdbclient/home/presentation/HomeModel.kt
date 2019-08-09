package com.illiarb.tmdbclient.home.presentation

import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.ListSection
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieBlock
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.navigation.*
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
@InternalCoroutinesApi
@FlowPreview
class HomeModel @Inject constructor(
    featureConfig: FeatureConfig,
    private val moviesInteractor: MoviesInteractor,
    private val authenticator: Authenticator,
    private val router: Router
) : BasePresentationModel() {

    private val sections = Channel<List<MovieSection>>()
    private val isAccountEnabled = flowOf(featureConfig.isFeatureEnabled(FeatureName.AUTH))
    private val isSearchEnabled = flowOf(featureConfig.isFeatureEnabled(FeatureName.SEARCH))

    init {
        launch {
            moviesInteractor.getAllMovies().doOnSuccess {
                sections.send(createMovieSections(it))
            }
        }
    }

    fun bind(view: HomeView) {
        view.viewScope.launch {
            sections.consumeAsFlow().collect(view.movieSections)

            isAccountEnabled.collect(view.accountEnabled)
            isSearchEnabled.collect(view.searchEnabled)

            view.onMovieClick().collect {
                router.navigateTo(MovieDetailsScreen(it.id))
            }

            view.onAccountClick().collect {
                if (authenticator.isAuthenticated()) {
                    router.navigateTo(AccountScreen)
                } else {
                    router.navigateTo(AuthScreen)
                }
            }

            view.onSearchClick().collect {
                router.navigateTo(SearchScreen)
            }
        }
    }

    private fun createMovieSections(movies: List<MovieBlock>): List<MovieSection> =
        movies.map { (filter, movies) -> ListSection(filter.name, movies) }
}