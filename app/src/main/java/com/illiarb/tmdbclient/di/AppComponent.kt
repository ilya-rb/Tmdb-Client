package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.MobileApplication
import com.illiarb.tmdbclient.di.modules.AppModule
import com.illiarb.tmdbclient.di.modules.InteractorsModule
import com.illiarb.tmdbclient.di.modules.NavigationModule
import com.illiarb.tmdbclient.storage.di.StorageComponent
import com.illiarb.tmdbclient.tools.di.ToolsComponent
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [AppModule::class, NavigationModule::class, InteractorsModule::class],
    dependencies = [
        StorageProvider::class,
        ToolsProvider::class
    ]
)
@Singleton
interface AppComponent : AppProvider {

    companion object {

        fun get(app: App): AppProvider {
            val toolsProvider = ToolsComponent.get(app)
            val storageProvider = StorageComponent.get(app, toolsProvider)

            return DaggerAppComponent.builder()
                .storageProvider(storageProvider)
                .toolsProvider(toolsProvider)
                .appModule(AppModule(app))
                .build()
        }
    }

    fun inject(app: MobileApplication)
}