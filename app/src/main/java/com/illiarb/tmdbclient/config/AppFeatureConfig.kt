package com.illiarb.tmdbclient.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.illiarb.tmdbclient.BuildConfig
import com.illiarb.tmdbclient.R
import com.illiarb.tmdblcient.core.system.Logger
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureName
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 21.01.19.
 */
@Singleton
class AppFeatureConfig @Inject constructor() : FeatureConfig {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
        .also {
            it.setDefaults(R.xml.remote_config_defaults)
        }

    init {
        remoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        )
    }

    override fun fetchConfiguration() {
        remoteConfig.fetch()
            .addOnSuccessListener {
                Logger.i("Successfully fetched remote config")
                remoteConfig.activateFetched()
            }
            .addOnFailureListener {
                Logger.e("Failed to fetch remote config ${it.message}")
            }
    }

    override fun isFeatureEnabled(featureName: FeatureName): Boolean {
        return remoteConfig.getBoolean(featureName.code)
    }
}