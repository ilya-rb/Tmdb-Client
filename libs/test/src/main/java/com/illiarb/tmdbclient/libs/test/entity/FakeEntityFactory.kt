package com.illiarb.tmdbclient.libs.test.entity

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.Image
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieFilter
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor

/**
 * Factory class to create entities for tests
 *
 * @author ilya-rb on 24.01.19.
 */
@Suppress("MagicNumber")
object FakeEntityFactory {

  val movieFilters = listOf(
    MovieFilter("Now playing", MovieFilter.TYPE_NOW_PLAYING),
    MovieFilter("Popular", MovieFilter.TYPE_POPULAR),
    MovieFilter("Upcoming", MovieFilter.TYPE_UPCOMING),
    MovieFilter("Top rated", MovieFilter.TYPE_TOP_RATED)
  )

  private fun defaultMovieCreator(): () -> Movie = {
    Movie(
      123123,
      null,
      null,
      emptyList(),
      "http://google.com",
      emptyList(),
      "2018-01-12",
      "overview",
      emptyList(),
      120,
      "Movie title",
      emptyList(),
      7.4f,
      "",
      9,
      emptyList()
    )
  }

  fun getMoviesByType(
    type: String,
    refresh: Boolean
  ): Result<List<Movie>> {
    val size = 10

    return Result.Ok(
      mutableListOf<Movie>().apply {
        for (i in 0..size) {
          add(createFakeMovie())
        }
      }
    )
  }

  @Suppress("MagicNumber")
  private val testGenres = listOf(
    Genre(0, "Action"),
    Genre(1, "Drama"),
    Genre(2, "Animation"),
    Genre(3, "Comedy"),
    Genre(4, "Crime"),
    Genre(5, "Documentary"),
    Genre(6, "War"),
    Genre(7, "Thriller"),
    Genre(8, "Horror")
  )

  fun createGenre(id: Int = 1, title: String = "Drama"): Genre = Genre(id, title)

  fun createFakeMovie(init: () -> Movie = defaultMovieCreator()): Movie = init()

  fun createFakeMovieList(
    size: Int,
    creator: () -> Movie = defaultMovieCreator()
  ): List<Movie> = mutableListOf<Movie>().apply {
    for (i in 0..size) {
      add(creator.invoke())
    }
  }

  fun getGenres(): Result<List<Genre>> {
    return Result.Ok(testGenres)
  }

  fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie> {
    var movie = createFakeMovie()
    if (appendToResponse.contains(MoviesInteractor.KEY_INCLUDE_IMAGES)) {
      movie = movie.copy(
        images = listOf(
          Image("image1", "image", emptyList()),
          Image("image1", "image", emptyList())
        )
      )
    }
    return Result.Ok(movie)
  }
}