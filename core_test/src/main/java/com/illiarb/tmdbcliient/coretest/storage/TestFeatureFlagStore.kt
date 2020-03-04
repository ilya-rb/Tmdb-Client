package com.illiarb.tmdbcliient.coretest.storage

import com.illiarb.tmdblcient.core.storage.FeatureFlagStore

class TestFeatureFlagStore : FeatureFlagStore {

  override fun isFeatureEnabled(featureName: FeatureFlagStore.FeatureFlag): Boolean =
    when (featureName) {
      FeatureFlagStore.FeatureFlag.AUTH -> false
    }
}