package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.appinitializers.TmdbInitializer
import com.illiarb.tmdblcient.core.app.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface AppInitializerModule {

    @Binds
    @IntoSet
    fun bindTmdbInitializer(tmdbInitializer: TmdbInitializer): AppInitializer
}