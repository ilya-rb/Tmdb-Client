package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.di.modules.UseCaseModule
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import com.illiarb.tmdblcient.core.di.providers.UseCaseProvider
import dagger.Component

/**
 * @author ilya-rb on 08.01.19.
 */
@Component(
    dependencies = [
        StorageProvider::class,
        ToolsProvider::class
    ],
    modules = [UseCaseModule::class]
)
interface UseCaseComponent : UseCaseProvider {

    companion object {
        fun get(storageProvider: StorageProvider, toolsProvider: ToolsProvider): UseCaseProvider =
            DaggerUseCaseComponent.builder()
                .storageProvider(storageProvider)
                .toolsProvider(toolsProvider)
                .build()
    }
}