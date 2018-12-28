package com.illiarb.tmdbclient.feature.home.list.interactor

import com.illiarb.tmdblcient.core.entity.ListSection
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.entity.NowPlayingSection
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.modules.movie.HomeInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.SearchScreen
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
class HomeInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val schedulerProvider: SchedulerProvider,
    private val authenticator: Authenticator,
    private val router: Router
) : HomeInteractor {

    override fun getMovieSections(): Single<List<MovieSection>> =
        getMovieFilters()
            .flatMap { filters ->
                Single.just(filters.take(2).map { filter ->
                    moviesRepository.getMoviesByType(filter.code)
                        .map {
                            // Set now playing filter as main
                            if (filter.code == MovieFilter.TYPE_NOW_PLAYING) {
                                NowPlayingSection(filter.name, it)
                            } else {
                                ListSection(filter.name, it)
                            }
                        }
                        .subscribeOn(schedulerProvider.provideIoScheduler())
                        .blockingGet()
                })
            }

    override fun getMovieFilters(): Single<List<MovieFilter>> = moviesRepository.getMovieFilters()

    override fun onMovieSelected(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }

    override fun onSearchClicked() {
        router.navigateTo(SearchScreen)
    }

    override fun onAccountClicked() {
        if (authenticator.isAuthenticated()) {
            router.navigateTo(AccountScreen)
        } else {
            router.navigateTo(SearchScreen)
        }
    }
}