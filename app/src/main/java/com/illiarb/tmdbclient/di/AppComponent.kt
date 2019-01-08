package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.MobileApplication
import com.illiarb.tmdbclient.di.modules.AppModule
import com.illiarb.tmdbclient.storage.di.StorageComponent
import com.illiarb.tmdbclient.tools.di.ToolsComponent
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import com.illiarb.tmdblcient.core.di.providers.UseCaseProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [
        UseCaseProvider::class,
        StorageProvider::class,
        ToolsProvider::class
    ],
    modules = [AppModule::class]
)
@Singleton
interface AppComponent : AppProvider {

    companion object {

        fun get(app: App): AppProvider {
            val toolsProvider = ToolsComponent.get()
            val storageProvider = StorageComponent.get(app, toolsProvider)
            val useCaseProvider = UseCaseComponent.get(storageProvider, toolsProvider)

            return DaggerAppComponent.builder()
                .storageProvider(storageProvider)
                .useCaseProvider(useCaseProvider)
                .toolsProvider(toolsProvider)
                .appModule(AppModule(app))
                .build()
        }
    }

    fun inject(app: MobileApplication)
}