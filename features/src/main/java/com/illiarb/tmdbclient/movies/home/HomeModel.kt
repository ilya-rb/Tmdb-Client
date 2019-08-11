package com.illiarb.tmdbclient.movies.home

import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.ListSection
import com.illiarb.tmdblcient.core.domain.entity.MovieBlock
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.ext.launchAndCollect
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    featureConfig: FeatureConfig,
    private val moviesInteractor: MoviesInteractor,
    private val authenticator: Authenticator,
    private val router: Router
) : BasePresentationModel() {

    private val isAccountEnabled = flowOf(featureConfig.isFeatureEnabled(FeatureName.AUTH))
    private val progressVisible = Channel<Boolean>(Channel.CONFLATED)

    private val sections: Flow<List<MovieSection>> =
        flow { emit(createMovieSections(moviesInteractor.getAllMovies())) }
            .onStart { progressVisible.send(true) }
            .onCompletion { progressVisible.send(false) }

    fun bind(view: HomeView) {
        view.launchAndCollect(progressVisible.consumeAsFlow()) { view.progressVisible(it) }

        view.launchAndCollect(isAccountEnabled) { view.accountEnabled(it) }

        view.launchAndCollect(sections) { view.movieSections(it) }

        view.launchAndCollect(view.uiEvents) {
            when (it) {
                is HomeView.UiEvent.MovieClick -> router.navigateTo(MovieDetailsScreen(it.movie.id))
                is HomeView.UiEvent.AccountClick -> {
                    if (authenticator.isAuthenticated()) {
                        router.navigateTo(AccountScreen)
                    } else {
                        router.navigateTo(AuthScreen)
                    }
                }
            }
        }
    }

    private fun createMovieSections(movies: List<MovieBlock>): List<MovieSection> =
        movies.map { (filter, movies) -> ListSection(filter.name, movies) }
}