package com.illiarb.tmdbclient.feature.search

/**
 * @author ilya-rb on 27.12.18.
 */
sealed class SearchIntent {
    data class Search(val query: String) : SearchIntent()
    data class ShowMovieDetails(val id: Int) : SearchIntent()
}