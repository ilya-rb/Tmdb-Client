package com.illiarb.tmdblcient.core.system.feature

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Interface for interacting
 * with dynamic feature modules
 *
 * @author ilya-rb on 24.12.18.
 */
interface FeatureInstaller {

    fun installFeatures(vararg featureName: DynamicFeatureName): Observable<FeatureInstallState>

    fun deleteFeature(): Completable

    fun isFeatureInstalled(): Single<Boolean>

    fun mockInstallFeatures(vararg featureName: DynamicFeatureName): Observable<FeatureInstallState>
}