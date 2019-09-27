package com.illiarb.tmdblcient.core.feature.dynamic

/**
 * Interface for interacting
 * with dynamic feature modules
 *
 * @author ilya-rb on 24.12.18.
 */
interface FeatureInstaller {

    suspend fun installFeatures(vararg featureName: DynamicFeatureName): FeatureInstallState

    fun mockInstallFeatures(vararg featureName: DynamicFeatureName): FeatureInstallState

    fun deleteFeature(): Boolean

    fun isFeatureInstalled(): Boolean
}