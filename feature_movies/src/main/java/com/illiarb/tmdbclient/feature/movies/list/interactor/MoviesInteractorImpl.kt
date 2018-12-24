package com.illiarb.tmdbclient.feature.movies.list.interactor

import com.illiarb.tmdblcient.core.entity.ListSection
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.entity.NowPlayingSection
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
class MoviesInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val schedulerProvider: SchedulerProvider,
    private val router: Router
) : MoviesInteractor {

    override fun getMovieSections(): Single<List<MovieSection>> =
        getMovieFilters()
            .flatMap { filters ->
                Single.just(filters.map { filter ->
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

    override fun onMovieSelected(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }

    private fun getMovieFilters(): Single<List<MovieFilter>> = moviesRepository.getMovieFilters()
}