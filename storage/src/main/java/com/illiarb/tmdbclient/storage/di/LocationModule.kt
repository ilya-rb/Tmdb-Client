package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.local.location.AndroidLocationService
import com.illiarb.tmdbexplorerdi.App
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 02.11.18.
 */
@Module
class LocationModule(val app: App) {

    @Provides
    fun provideAndroidLocationService(): AndroidLocationService = AndroidLocationService(app)
}