package com.illiarb.tmdblcient.core.domain.entity

/**
 * @author ilya-rb on 04.11.18.
 */
sealed class MovieSection(val title: String, val movies: List<Movie>)

class ListSection(title: String, movies: List<Movie>) : MovieSection(title, movies)
class NowPlayingSection(title: String, movies: List<Movie>) : MovieSection(title, movies)