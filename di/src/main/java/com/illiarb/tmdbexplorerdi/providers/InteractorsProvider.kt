package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.modules.AppInteractor
import com.illiarb.tmdblcient.core.modules.account.AccountInteractor
import com.illiarb.tmdblcient.core.modules.auth.AuthInteractor
import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor

interface InteractorsProvider {

    fun provideMoviesInteractor(): MoviesInteractor

    fun provideMovieDetailsInteractor(): MovieDetailsInteractor

    fun provideAccountInteractor(): AccountInteractor

    fun provideAuthInteractor(): AuthInteractor

    fun provideAppInteractor(): AppInteractor
}