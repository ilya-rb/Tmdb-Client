package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.modules.account.AccountInteractor
import com.illiarb.tmdblcient.core.modules.auth.AuthInteractor
import com.illiarb.tmdblcient.core.modules.explore.ExploreInteractor
import com.illiarb.tmdblcient.core.modules.main.MainInteractor
import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor

interface InteractorsProvider {

    fun provideMoviesInteractor(): MoviesInteractor

    fun provideMovieDetailsInteractor(): MovieDetailsInteractor

    fun provideExploreInteractor(): ExploreInteractor

    fun provideMainInteractor(): MainInteractor

    fun provideAccountInteractor(): AccountInteractor

    fun provideAuthInteractor(): AuthInteractor
}