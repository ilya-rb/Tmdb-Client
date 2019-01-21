package com.illiarb.tmdbclient.config

import com.illiarb.tmdblcient.core.config.FeatureConfig
import com.illiarb.tmdblcient.core.config.FeatureName
import javax.inject.Inject

/**
 * @author ilya-rb on 21.01.19.
 */
class AppFeatureConfig @Inject constructor() : FeatureConfig {

    // TODO Add Firebase remote config
    override fun isFeatureEnabled(featureName: FeatureName): Boolean =
        when (featureName) {
            FeatureName.AUTH -> false
            FeatureName.SEARCH -> true
        }
}