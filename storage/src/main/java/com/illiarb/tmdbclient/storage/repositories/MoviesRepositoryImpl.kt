package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.movies.MoviesStorage
import com.illiarb.tmdbclient.storage.mappers.MovieMapper
import com.illiarb.tmdbclient.storage.mappers.ReviewMapper
import com.illiarb.tmdbclient.storage.network.api.movie.MoviesApi
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 25.10.18.
 */
class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesStorage: MoviesStorage,
    private val movieMapper: MovieMapper,
    private val reviewMapper: ReviewMapper
) : MoviesRepository {

    override fun getMoviesByType(type: String): Single<List<Movie>> =
        moviesStorage.getMoviesByType(type)
            .flatMap {
                if (it.isNotEmpty()) {
                    Single.just(it)
                } else {
                    moviesApi.getMoviesByType(type)
                        .doOnSuccess { movies ->
                            moviesStorage.storeMovies(type, movies)
                        }
                }
            }
            .map(movieMapper::mapList)

    override fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie> =
        moviesApi.getMovieDetails(id, appendToResponse).map(movieMapper::map)

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesApi.getMovieReviews(id).map(reviewMapper::mapList)
}