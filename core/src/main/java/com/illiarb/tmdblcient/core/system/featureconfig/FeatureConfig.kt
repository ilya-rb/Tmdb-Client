package com.illiarb.tmdblcient.core.system.featureconfig

/**
 * @author ilya-rb on 21.01.19.
 */
interface FeatureConfig {

    fun isFeatureEnabled(featureName: FeatureName): Boolean

    fun fetchConfiguration()
}