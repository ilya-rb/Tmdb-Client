package com.illiarb.tmdbcliient.coretest.tools

import com.illiarb.tmdbclient.tools.FeatureFlagStore

class TestFeatureFlagStore : FeatureFlagStore {

  override fun isFeatureEnabled(featureName: FeatureFlagStore.FeatureFlag): Boolean =
    when (featureName) {
      FeatureFlagStore.FeatureFlag.AUTH -> false
    }
}