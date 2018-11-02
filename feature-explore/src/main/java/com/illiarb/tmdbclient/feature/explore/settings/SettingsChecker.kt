package com.illiarb.tmdbclient.feature.explore.settings

import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.illiarb.tmdblcient.core.ext.onCompleteSafe
import io.reactivex.Completable
import javax.inject.Inject

/**
 * @author ilya-rb on 01.11.18.
 */
class SettingsChecker @Inject constructor(private val activity: FragmentActivity) {

    private val settingsClient = LocationServices.getSettingsClient(activity)

    fun checkLocationSettings(): Completable {
        return Completable.create { emitter ->
            @Suppress("DEPRECATION")
            LocationServices.SettingsApi.checkLocationSettings(settingsClient.asGoogleApiClient(), createLocationRequest())
                .addStatusListener {
                    when (it.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                if (it.hasResolution()) {
                                    SettingsResolutionActivity.startSettingsResolution(activity, it)
                                }
                            } catch (ex: IntentSender.SendIntentException) {
                                // Ignore error
                                emitter.onCompleteSafe()
                            }
                        }
                        else -> emitter.onCompleteSafe()
                    }
                }
        }
    }

    private fun createLocationRequest(): LocationSettingsRequest =
        LocationSettingsRequest.Builder()
            .addLocationRequest(
                LocationRequest().apply {
                    priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                }
            )
            // http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
            .setAlwaysShow(true)
            .build()
}