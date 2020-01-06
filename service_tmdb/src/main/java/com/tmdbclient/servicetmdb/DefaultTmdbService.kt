package com.tmdbclient.servicetmdb

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.ListSection
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.api.TrendingApi
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.mappers.TrendingMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DefaultTmdbService @Inject constructor(
    private val repository: MoviesRepository,
    private val movieMapper: MovieMapper,
    private val genresRepository: GenresRepository,
    private val discoverApi: DiscoverApi,
    private val trendingApi: TrendingApi,
    private val trendingMapper: TrendingMapper
) : TmdbService {

    companion object {
        // Max genres displayed in the section
        private const val GENRES_MAX_SIZE = 8
    }

    override suspend fun getMovieSections(): Result<List<MovieSection>> = coroutineScope {
        Result.create {
            val movies = async { getAllMovies().getOrThrow() }
            val genres = async { getMovieGenres().getOrThrow() }
            val trending = async { getTrending().getOrThrow() }
            createSections(movies.await(), genres.await(), trending.await())
        }
    }

    override suspend fun getMovieDetails(id: Int): Result<Movie> =
        Result.create { repository.getMovieDetails(id, "images,reviews") }

    override suspend fun getMovieReviews(id: Int): Result<List<Review>> =
        Result.create { repository.getMovieReviews(id) }

    override suspend fun getMovieGenres(): Result<List<Genre>> =
        Result.create { genresRepository.getGenres() }

    override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> = Result.create {
        val results = if (genreId == -1) {
            discoverApi.discoverMoviesAsync().await()
        } else {
            discoverApi.discoverMoviesAsync(genreId.toString()).await()
        }
        movieMapper.mapList(results.results)
    }

    private suspend fun getAllMovies(): Result<List<MovieBlock>> = Result.create {
        repository.getMovieFilters().map { MovieBlock(it, getMoviesByType(it)) }
    }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, false)

    private suspend fun getTrending(): Result<TrendingSection> = Result.create {
        val results = trendingApi.getTrendingAsync(
            TrendingApi.TRENDING_TYPE_ALL,
            TrendingApi.TRENDING_THIS_WEEK
        ).await()

        TrendingSection(trendingMapper.mapList(results.results))
    }

    private fun createSections(
        movieBlocks: List<MovieBlock>,
        genres: List<Genre>,
        trending: TrendingSection
    ): List<MovieSection> {
        val result = mutableListOf<MovieSection>()
        val blocksWithMovies = movieBlocks.filter { it.movies.isNotEmpty() }

        blocksWithMovies.forEach { block ->
            // Now playing section should always be on top
            if (block.filter.isNowPlaying()) {
                result.add(0, NowPlayingSection(block.filter.name, block.movies))
            } else {
                result.add(ListSection(block.filter.code, block.filter.name, block.movies))
            }
        }

        if (genres.isNotEmpty()) {
            if (genres.size > GENRES_MAX_SIZE) {
                result.add(1, GenresSection(genres.subList(0, GENRES_MAX_SIZE)))
            } else {
                result.add(1, GenresSection(genres))
            }
        }

        if (trending.items.isNotEmpty()) {
            result.add(0, trending)
        }

        return result
    }
}