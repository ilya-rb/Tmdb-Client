package com.illiarb.tmdblcient.core.domain

import java.util.Collections

/**
 * @author ilya-rb on 04.11.18.
 */
sealed class MovieSection(val title: String, val movies: List<Movie>)

class ListSection(title: String, movies: List<Movie>) : MovieSection(title, movies)
class NowPlayingSection(title: String, movies: List<Movie>) : MovieSection(title, movies)
class SearchResult(val movie: Movie) : MovieSection("", Collections.emptyList())