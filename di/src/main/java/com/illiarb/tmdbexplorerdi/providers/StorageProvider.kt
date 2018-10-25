package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository

interface StorageProvider {

    fun provideMoviesRepository(): MoviesRepository
}