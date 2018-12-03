package com.illiarb.tmdbclient.di.modules

import androidx.work.WorkerFactory
import com.illiarb.tmdbclient.storage.local.AndroidResourceResolver
import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.local.location.AndroidLocationService
import com.illiarb.tmdbclient.storage.network.api.service.ConfigurationService
import com.illiarb.tmdbclient.storage.system.AndroidWorkManager
import com.illiarb.tmdbclient.storage.system.DaggerWorkerFactory
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
class StorageModule(val app: App) {

    @Provides
    @Singleton
    fun providePersistableStore(): PersistableStorage = PersistableStorage(app)

    @Provides
    @Singleton
    fun provideResourceResolver(): ResourceResolver = AndroidResourceResolver(app)

    @Provides
    @Singleton
    fun provideWorkManager(workerFactory: WorkerFactory): WorkManager = AndroidWorkManager(app, workerFactory)

    @Provides
    fun provideAndroidLocationService(): AndroidLocationService = AndroidLocationService(app)

    @Provides
    fun provideWorkerFactory(configurationService: ConfigurationService, persistableStorage: PersistableStorage): WorkerFactory =
        DaggerWorkerFactory(configurationService, persistableStorage)
}