package com.illiarb.tmdbcliient.core_test.storage

import com.illiarb.tmdblcient.core.feature.FeatureFlag
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore

class TestFeatureFlagStore : FeatureFlagStore {

    override fun isFeatureEnabled(featureName: FeatureFlag): Boolean =
        when (featureName) {
            FeatureFlag.AUTH -> false
        }
}