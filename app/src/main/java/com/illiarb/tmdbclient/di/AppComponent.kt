package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.di.modules.AppModule
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import com.illiarb.tmdbexplorerdi.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [
        InteractorsProvider::class,
        StorageProvider::class,
        ToolsProvider::class
    ],
    modules = [AppModule::class]
)
@Singleton
interface AppComponent : AppProvider {

    companion object {

        fun get(app: App): AppProvider {
            val storageProvider = StorageComponent.get(app)
            val toolsProvider = ToolsComponent.get()
            val interactorsProvider = InteractorsComponent.get(storageProvider, toolsProvider)

            return DaggerAppComponent.builder()
                .storageProvider(storageProvider)
                .interactorsProvider(interactorsProvider)
                .toolsProvider(toolsProvider)
                .appModule(AppModule(app))
                .build()
        }
    }
}