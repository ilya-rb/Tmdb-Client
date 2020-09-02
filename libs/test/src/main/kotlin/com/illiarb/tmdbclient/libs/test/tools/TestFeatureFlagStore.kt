package com.illiarb.tmdbclient.libs.test.tools

import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore

class TestFeatureFlagStore : FeatureFlagStore {

  override fun isFeatureEnabled(featureName: FeatureFlagStore.FeatureFlag): Boolean =
    when (featureName) {
      FeatureFlagStore.FeatureFlag.AUTH -> false
      FeatureFlagStore.FeatureFlag.MOVIE_DETAILS_COMPOSE -> false
    }
}