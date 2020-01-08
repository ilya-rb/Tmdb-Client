package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor

interface InteractorsProvider {

    fun provideMoviesInteractor(): MoviesInteractor

    fun provideGenresInteractor(): GenresInteractor

    fun provideHomeInteractor(): HomeInteractor

    fun provideTrendingInteractor(): TrendingInteractor
}