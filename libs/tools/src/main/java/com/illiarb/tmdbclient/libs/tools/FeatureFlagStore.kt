package com.illiarb.tmdbclient.libs.tools

/**
 * @author ilya-rb on 21.01.19.
 */
interface FeatureFlagStore {

  fun isFeatureEnabled(featureName: FeatureFlag): Boolean

  enum class FeatureFlag(val code: String) {

    AUTH("auth_enabled")
  }
}

