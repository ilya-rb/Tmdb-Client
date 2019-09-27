package com.illiarb.tmdbclient.details

import androidx.lifecycle.LiveData
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.util.Async

interface MovieDetailsViewModel {

    val movie: LiveData<Async<Movie>>
}