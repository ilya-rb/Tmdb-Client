package com.illiarb.tmdbclient.home.presentation

import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface HomeView {

    val viewScope: CoroutineScope

    val movieSections: FlowCollector<List<MovieSection>>
    val searchEnabled: FlowCollector<Boolean>
    val accountEnabled: FlowCollector<Boolean>

    fun onMovieClick(): Flow<Movie>
    fun onAccountClick(): Flow<Unit>
    fun onSearchClick(): Flow<Unit>
}