package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.SearchInteractor

/**
 * @author ilya-rb on 18.02.19.
 */
interface InteractorsProvider {

    fun moviesInteractor(): MoviesInteractor

    fun searchInteractor(): SearchInteractor

}