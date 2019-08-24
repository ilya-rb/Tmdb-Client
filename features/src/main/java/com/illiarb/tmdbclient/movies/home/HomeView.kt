package com.illiarb.tmdbclient.movies.home

import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface HomeView : CoroutineScope {

    val uiEvents: Flow<SingleUiEvent>

    val searchQuery: Flow<CharSequence>

    val searchFocusChange: Flow<Boolean>

    fun movieSections(state: MovieSectionsState)

    fun accountVisible(visible: Boolean)

    fun searchResultsState(state: SearchResultState)

    sealed class SingleUiEvent {
        data class MovieClick(val movie: Movie) : SingleUiEvent()
        object ClearSearch : SingleUiEvent()
        object AccountClick : SingleUiEvent()
    }

    sealed class SearchResultState {
        object Hidden : SearchResultState()
        object Empty : SearchResultState()
        data class Results(val results: List<MovieSection>) : SearchResultState()
    }

    sealed class MovieSectionsState {
        object Loading : MovieSectionsState()
        object Failure : MovieSectionsState()
        data class Content(val sections: List<MovieSection>) : MovieSectionsState()
    }

}