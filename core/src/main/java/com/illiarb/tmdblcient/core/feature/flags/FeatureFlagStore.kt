package com.illiarb.tmdblcient.core.feature.flags

/**
 * @author ilya-rb on 21.01.19.
 */
interface FeatureFlagStore {

    fun isFeatureEnabled(featureName: FeatureFlag): Boolean
}