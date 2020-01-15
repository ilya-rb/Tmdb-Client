package com.illiarb.tmdbclient.storage

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore.FeatureFlag
import com.illiarb.tmdblcient.core.tools.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseFeatureFlagStore @Inject constructor() : FeatureFlagStore {

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

    override fun isFeatureEnabled(featureName: FeatureFlag): Boolean =
        remoteConfig.getBoolean(featureName.code)
}