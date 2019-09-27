package com.illiarb.tmdblcient.core.feature.dynamic

/**
 * @author ilya-rb on 24.12.18.
 */
data class FeatureInstallState(
    val featureName: String,
    val status: FeatureDownloadStatus,
    val percentDownloaded: Long
)