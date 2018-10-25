package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.resources.AndroidResourceResolver
import com.illiarb.tmdbclient.scheduler.AndroidSchedulerProvider
import com.illiarb.tmdbclient.storage.di.StorageComponent
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(
    modules = [AppModule::class],
    dependencies = [
        InteractorsProvider::class,
        StorageProvider::class
    ]
)
@Singleton
interface AppComponent : AppProvider {

    companion object {

        fun get(app: App): AppProvider {
            val storageProvider = StorageComponent.get()
            val interactorsProvider = InteractorsComponent.get(storageProvider)

            return DaggerAppComponent.builder()
                .storageProvider(storageProvider)
                .interactorsProvider(interactorsProvider)
                .appModule(AppModule(app))
                .build()
        }
    }
}

@Module
class AppModule(val app: App) {

    @Provides
    fun provideApp(): App = app

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AndroidSchedulerProvider()

    @Provides
    fun provideResourceResolver(): ResourceResolver = AndroidResourceResolver(app.getApplication())
}