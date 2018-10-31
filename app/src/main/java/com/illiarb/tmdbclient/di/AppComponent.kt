package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.navigation.AppNavigator
import com.illiarb.tmdbclient.storage.di.StorageComponent
import com.illiarb.tmdbclient.system.AndroidEventBus
import com.illiarb.tmdbclient.system.RxSchedulerProvider
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.system.EventBus
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
            val storageProvider = StorageComponent.get(app)
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
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideEventBus(): EventBus = AndroidEventBus()

    @Provides
    @Singleton
    fun provideNavigator(eventBus: EventBus): Navigator = AppNavigator(eventBus)

    @Provides
    fun provideAppNavigationGraphRes(): Int = R.navigation.app_nav_graph
}