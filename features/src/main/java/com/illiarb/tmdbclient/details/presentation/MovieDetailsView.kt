package com.illiarb.tmdbclient.details.presentation

import com.illiarb.tmdblcient.core.domain.entity.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface MovieDetailsView {

    val movieCollector: FlowCollector<Movie>
    val viewScope: CoroutineScope

    fun reviewsClick(): Flow<Unit>
}