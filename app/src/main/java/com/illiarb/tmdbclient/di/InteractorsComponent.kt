package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.coreimpl.modules.movie.MoviesInteractorImpl
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.NetworkProvider
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [NetworkProvider::class],
    modules = [InteractorsModule::class]
)
interface InteractorsComponent : InteractorsProvider {

    companion object {

        fun get(networkProvider: NetworkProvider): InteractorsProvider {
            return DaggerInteractorsComponent.builder()
                .networkProvider(networkProvider)
                .build()
        }
    }
}

@Module
interface InteractorsModule {

    @Binds
    fun bindMoviesInteractor(impl: MoviesInteractorImpl): MoviesInteractor
}