package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.feature.flags.FeatureFlagStore
import com.illiarb.tmdblcient.core.storage.ResourceResolver

/**
 * @author ilya-rb on 24.12.18.
 */
interface StorageProvider {

    fun provideResourceResolver(): ResourceResolver

    fun provideFeatureFlagStore(): FeatureFlagStore
}