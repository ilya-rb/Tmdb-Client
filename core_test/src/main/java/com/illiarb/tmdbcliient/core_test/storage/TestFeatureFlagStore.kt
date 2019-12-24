package com.illiarb.tmdbcliient.core_test.storage

import com.illiarb.tmdblcient.core.feature.FeatureFlagStore

class TestFeatureFlagStore : FeatureFlagStore {

    override fun isFeatureEnabled(featureName: FeatureFlagStore.FeatureFlag): Boolean =
        when (featureName) {
            FeatureFlagStore.FeatureFlag.AUTH -> false
        }
}