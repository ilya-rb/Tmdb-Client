package com.illiarb.tmdbclient.libs.tools.internal

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.illiarb.tmdbclient.libs.logger.Logger
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.R
import javax.inject.Inject

internal class FirebaseFeatureFlagStore @Inject constructor() : FeatureFlagStore {

  private val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
    setDefaultsAsync(R.xml.remote_config_defaults)
      .addOnFailureListener { Logger.e("Failed to set remote config defaults $it") }
  }

  init {
    remoteConfig.fetchAndActivate()
      .addOnSuccessListener { Logger.i("Successfully fetched remote config") }
      .addOnFailureListener { Logger.e("Failed to fetch remote config ${it.message}") }
  }

  override fun isFeatureEnabled(featureName: FeatureFlagStore.FeatureFlag): Boolean =
    remoteConfig.getBoolean(featureName.code)
}