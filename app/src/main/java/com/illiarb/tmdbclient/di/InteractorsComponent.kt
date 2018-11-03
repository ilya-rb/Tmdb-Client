package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.di.modules.InteractorsModule
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import dagger.Component

@Component(
    dependencies = [StorageProvider::class],
    modules = [InteractorsModule::class]
)
interface InteractorsComponent : InteractorsProvider {

    companion object {

        fun get(storageProvider: StorageProvider): InteractorsProvider {
            return DaggerInteractorsComponent.builder()
                .storageProvider(storageProvider)
                .build()
        }
    }
}