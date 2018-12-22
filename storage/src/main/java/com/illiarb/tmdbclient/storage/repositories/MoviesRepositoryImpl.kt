package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.mappers.MovieMapper
import com.illiarb.tmdbclient.storage.mappers.ReviewMapper
import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.network.api.service.MovieService
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import com.illiarb.tmdblcient.core.system.ResourceResolver
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 25.10.18.
 */
@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val moviesService: MovieService,
    private val persistableStorage: PersistableStorage,
    private val movieMapper: MovieMapper,
    private val reviewMapper: ReviewMapper,
    private val resourceResolver: ResourceResolver
) : MoviesRepository {

    /**
     * As repository is a singleton
     * data refresh will be once per app starting
     */
    private var needsRefresh = true

    override fun getMoviesByType(type: String): Single<List<Movie>> {
        val onSuccessAction: (List<MovieModel>) -> Unit = {
            persistableStorage.storeMovies(type, it)
            needsRefresh = false
        }

        return persistableStorage.getMoviesByType(type)
            .flatMap { movies ->
                if (needsRefresh) {
                    moviesService.getMoviesByType(type)
                        .map { it.results }
                        .doOnSuccess(onSuccessAction)
                        .onErrorResumeNext {
                            if (movies.isNotEmpty()) {
                                Single.just(movies)
                            } else {
                                Single.error(it)
                            }
                        }
                } else {
                    if (movies.isNotEmpty()) {
                        Single.just(movies)
                    } else {
                        moviesService.getMoviesByType(type)
                            .map { it.results }
                            .doOnSuccess(onSuccessAction)
                    }
                }
            }
            .map(movieMapper::mapList)
    }

    override fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie> =
        moviesService.getMovieDetails(id, appendToResponse)
//        persistableStorage.getLastMovieDetails()
//            .flatMap {
//                // If Last stored differed of requested
//                // Fetch from network and store it
//                if (it.id != id) {
//                    moviesService.getMovieDetails(id, appendToResponse)
//                        .doOnSuccess { movie -> persistableStorage.storeLastMovieDetails(movie) }
//                } else {
//                    // Otherwise passed cached value
//                    Single.just(it)
//                }
//            }
            .map(movieMapper::map)

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesService.getMovieReviews(id)
            .map { it.results }
            .map(reviewMapper::mapList)

    override fun getMovieFilters(): Single<List<MovieFilter>> =
        Single.just(
            resourceResolver
                .getStringArray(R.array.movie_filters)
                .map { MovieFilter(it, it.toLowerCase().replace(" ", "_")) }
        )
}