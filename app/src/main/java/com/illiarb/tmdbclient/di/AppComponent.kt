package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.network.di.NetworkComponent
import com.illiarb.tmdbclient.resources.AndroidResourceResolver
import com.illiarb.tmdbclient.scheduler.AndroidSchedulerProvider
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.NetworkProvider
import com.illiarb.tmdblcient.core.resources.ResourceResolver
import com.illiarb.tmdblcient.core.schedulers.SchedulerProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(
    modules = [AppModule::class],
    dependencies = [
        InteractorsProvider::class,
        NetworkProvider::class
    ]
)
@Singleton
interface AppComponent : AppProvider {

    companion object {

        fun get(app: App): AppProvider {
            val networkProvider = NetworkComponent.get()
            val interactorsProvider = InteractorsComponent.get(networkProvider)

            return DaggerAppComponent.builder()
                .networkProvider(networkProvider)
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