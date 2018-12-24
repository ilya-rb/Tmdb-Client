package com.illiarb.tmdbclient.system

import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.illiarb.tmdbclient.R
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.feature.DynamicFeatureName
import com.illiarb.tmdblcient.core.system.feature.FeatureDownloadStatus.*
import com.illiarb.tmdblcient.core.system.feature.FeatureInstallState
import com.illiarb.tmdblcient.core.system.feature.FeatureInstaller
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

/**
 * Rx Wrapper of [SplitInstallManagerFactory]
 *
 * TODO:
 * 1. Add error handling
 * 2. Add [deleteFeature] implementation
 * 3. Add [isFeatureInstalled] implementation
 *
 * @author ilya-rb on 24.12.18.
 */
class RxFeatureInstaller @Inject constructor(
    app: App,
    private val resourceResolver: ResourceResolver
) : FeatureInstaller {

    private val splitInstallManager = SplitInstallManagerFactory.create(app.getApplication())

    /**
     * Set for keeping current downloads
     */
    private val currentDownloads by lazy(NONE) {
        Collections.newSetFromMap(ConcurrentHashMap<Int, Boolean>())
    }

    override fun installFeatures(vararg featureName: DynamicFeatureName): Observable<FeatureInstallState> =
        Observable.create { emitter ->
            val builder = SplitInstallRequest.newBuilder()

            featureName.forEach {
                builder.addModule(resolveFeatureName(it))
            }

            val request = builder.build()
            val installStateUpdateListener = SplitInstallStateUpdatedListener { newState ->
                if (currentDownloads.contains(newState.sessionId())) {
                    if (!emitter.isDisposed) {
                        emitter.onNext(createFeatureInstallState(createFeatureName(*featureName), newState))
                    }
                }
            }

            splitInstallManager.registerListener(installStateUpdateListener)
            splitInstallManager.startInstall(request)
                // When the platform accepts your request to download
                // an on demand module, it binds it to the following session ID.
                // You use this ID to track further status updates for the request.
                .addOnSuccessListener { id -> currentDownloads.add(id) }
                .addOnFailureListener {
                    if (!emitter.isDisposed) {
                        emitter.onError(it)
                    }
                }
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }

            emitter.setCancellable {
                splitInstallManager.unregisterListener(installStateUpdateListener)
            }
        }

    override fun mockInstallFeatures(vararg featureName: DynamicFeatureName): Observable<FeatureInstallState> =
        Observable.just(
            FeatureInstallState(
                featureName = createFeatureName(*featureName),
                percentDownloaded = 100,
                status = INSTALLED
            )
        )

    override fun deleteFeature(): Completable = TODO()

    override fun isFeatureInstalled(): Single<Boolean> = TODO()

    private fun createFeatureName(vararg features: DynamicFeatureName): String = features.joinToString(",") { it.name }

    private fun resolveFeatureName(featureName: DynamicFeatureName): String =
        when (featureName) {
            DynamicFeatureName.ACCOUNT -> resourceResolver.getString(R.string.dynamic_feature_account)
        }

    private fun createFeatureInstallState(featureName: String, newState: SplitInstallSessionState): FeatureInstallState {
        val progressPercentage = newState.totalBytesToDownload() / 100 * newState.bytesDownloaded()

        return when (newState.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> FeatureInstallState(featureName, DOWNLOADING, progressPercentage)
            SplitInstallSessionStatus.FAILED -> FeatureInstallState(featureName, FAILED, progressPercentage)
            SplitInstallSessionStatus.INSTALLED -> FeatureInstallState(featureName, INSTALLED, progressPercentage)
            else -> FeatureInstallState(featureName, UNKNOWN, progressPercentage)
        }
    }
}