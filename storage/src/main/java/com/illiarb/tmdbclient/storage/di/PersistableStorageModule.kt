package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbexplorerdi.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author ilya-rb on 26.10.18.
 */
@Module
class PersistableStorageModule(val app: App) {

    @Provides
    @Singleton
    fun providePersistableStore(): PersistableStorage = PersistableStorage(app)
}