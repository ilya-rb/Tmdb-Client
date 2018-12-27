package com.illiarb.tmdblcient.core.modules.search

import com.illiarb.tmdblcient.core.entity.Movie
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * @author ilya-rb on 26.12.18.
 */
interface SearchInteractor {

    sealed class SearchResult {
        object Loading : SearchResult()

        data class Success(val movies: List<Movie>) : SearchResult()
        data class Failure(val error: Throwable) : SearchResult()
    }

    sealed class Command {
        data class Search(val query: String) : Command()
        data class SearchMovieDetails(val id: Int) : Command()
    }

    sealed class SideEffect {
        data class ShowError(val message: String) : SideEffect()
    }

    val results: Observable<SearchResult>

    val sideEffects: Observable<SideEffect>

    val command: Consumer<Command>
}