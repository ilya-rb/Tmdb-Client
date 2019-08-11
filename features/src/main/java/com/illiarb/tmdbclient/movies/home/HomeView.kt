package com.illiarb.tmdbclient.movies.home

import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface HomeView : CoroutineScope {

    sealed class UiEvent {
        data class MovieClick(val movie: Movie): UiEvent()
        object AccountClick : UiEvent()
    }

    val uiEvents: Flow<UiEvent>

    fun movieSections(sections: List<MovieSection>)

    fun progressVisible(visible: Boolean)

    fun accountEnabled(enabled: Boolean)

}