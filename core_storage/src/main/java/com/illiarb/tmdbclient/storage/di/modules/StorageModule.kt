package com.illiarb.tmdbclient.storage.di.modules

import com.illiarb.tmdbclient.storage.FirebaseFeatureFlagStore
import com.illiarb.tmdbclient.storage.local.AndroidResourceResolver
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.feature.flags.FeatureFlagStore
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
class StorageModule(val app: App) {

    @Provides
    @Singleton
    fun provideResourceResolver(): ResourceResolver = AndroidResourceResolver(app)

    @Provides
    @Singleton
    fun provideFeatureFlagStore(): FeatureFlagStore = FirebaseFeatureFlagStore()
}