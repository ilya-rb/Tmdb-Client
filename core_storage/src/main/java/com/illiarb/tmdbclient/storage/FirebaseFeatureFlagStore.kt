package com.illiarb.tmdbclient.storage

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore.FeatureFlag
import com.illiarb.tmdblcient.core.tools.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseFeatureFlagStore @Inject constructor() : FeatureFlagStore {

    private val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
        setDefaults(R.xml.remote_config_defaults)
    }

    init {
        remoteConfig.fetchAndActivate()
            .addOnSuccessListener { Logger.i("Successfully fetched remote config") }
            .addOnFailureListener { Logger.e("Failed to fetch remote config ${it.message}") }
    }

    override fun isFeatureEnabled(featureName: FeatureFlag): Boolean =
        remoteConfig.getBoolean(featureName.code)
}