package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbexplorerdi.App
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
class AppModule(val app: App) {

    @Provides
    fun provideApp(): App = app
}