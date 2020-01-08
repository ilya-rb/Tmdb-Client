package com.illiarb.tmdblcient.core.interactor

import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.util.Result

interface HomeInteractor {

    companion object {
        // Max genres displayed in the section
        const val GENRES_MAX_SIZE = 8
    }

    suspend fun getHomeSections(): Result<List<MovieSection>>
}