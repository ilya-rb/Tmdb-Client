package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.local.AndroidResourceResolver
import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.system.ResourceResolver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author ilya-rb on 26.10.18.
 */
@Module
class StorageModule(val app: App) {

    @Provides
    @Singleton
    fun providePersistableStore(): PersistableStorage = PersistableStorage(app)

    @Provides
    @Singleton
    fun provideResourceResolver(): ResourceResolver = AndroidResourceResolver(app)
}