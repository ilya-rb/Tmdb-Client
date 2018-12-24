package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.MobileApplication
import com.illiarb.tmdbclient.di.modules.AppModule
import com.illiarb.tmdbclient.storage.di.StorageComponent
import com.illiarb.tmdbclient.tools.di.ToolsComponent
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.di.providers.InteractorsProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
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

    fun inject(app: MobileApplication)
}