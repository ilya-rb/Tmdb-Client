package com.illiarb.tmdbclient.services.tmdb.domain

/**
 * @author ilya-rb on 04.11.18.
 */
sealed class MovieSection

class ListSection(val code: String, val title: String, val movies: List<Movie>) : MovieSection()

class NowPlayingSection(val title: String, val movies: List<Movie>) : MovieSection()

class GenresSection(val genres: List<Genre>) : MovieSection()

class TrendingSection(val items: List<TrendingItem>) : MovieSection() {

  class TrendingItem(val movie: Movie)
}