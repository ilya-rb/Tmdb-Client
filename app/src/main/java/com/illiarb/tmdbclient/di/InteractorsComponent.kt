package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.coreimpl.modules.movie.MoviesInteractorImpl
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [StorageProvider::class],
    modules = [InteractorsModule::class]
)
interface InteractorsComponent : InteractorsProvider {

    companion object {

        fun get(storageProvider: StorageProvider): InteractorsProvider {
            return DaggerInteractorsComponent.builder()
                .storageProvider(storageProvider)
                .build()
        }
    }
}

@Module
interface InteractorsModule {

    @Binds
    fun bindMoviesInteractor(impl: MoviesInteractorImpl): MoviesInteractor
}