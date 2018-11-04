package com.illiarb.tmdbclient.coreimpl.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class MoviesInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val schedulerProvider: SchedulerProvider
) : MoviesInteractor {

    override fun getMovieSections(): Single<List<MovieSection>> =
        getMovieFilters()
            .flatMap { filters ->
                Single.just(filters.map { filter ->
                    moviesRepository.getMoviesByType(filter.code)
                        .map { MovieSection(filter.name, it) }
                        .subscribeOn(schedulerProvider.provideIoScheduler())
                        .blockingGet()
                })
            }

    override fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie> =
        moviesRepository.getMovieDetails(id, appendToResponse)

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesRepository.getMovieReviews(id)

    override fun getMovieFilters(): Single<List<MovieFilter>> =
        moviesRepository.getMovieFilters()
}