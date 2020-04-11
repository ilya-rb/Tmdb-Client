package com.illiarb.tmdbclient.libs.tools.internal

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.illiarb.tmdbclient.libs.logger.Logger
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseFeatureFlagStore @Inject constructor() : FeatureFlagStore {

  companion object {
    const val TAG = "FirebaseFeatureFlagStore"
  }

  private val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
    setDefaultsAsync(R.xml.remote_config_defaults)
      .addOnFailureListener { Logger.e(TAG, "Failed to set remote config defaults $it") }
  }

  init {
    remoteConfig.fetchAndActivate()
      .addOnSuccessListener { Logger.i(TAG, "Successfully fetched remote config") }
      .addOnFailureListener { Logger.e(TAG, "Failed to fetch remote config ${it.message}") }
  }

  override fun isFeatureEnabled(featureName: FeatureFlagStore.FeatureFlag): Boolean =
    remoteConfig.getBoolean(featureName.code)
}