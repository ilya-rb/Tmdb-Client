package com.illiarb.tmdbclient.storage.local.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.ext.onErrorSafe
import com.illiarb.tmdblcient.core.ext.onSuccessSafe
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 01.11.18.
 */
class AndroidLocationService @Inject constructor(app: App) {

    private val client = LocationServices.getFusedLocationProviderClient(app.getApplication())

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(): Single<Location> =
        Single.create { emitter ->
            client.lastLocation
                .addOnSuccessListener {
                    if (it != null) {
                        emitter.onSuccessSafe(it)
                    } else {
                        emitter.onSuccessSafe(Location(""))
                    }
                }
                .addOnFailureListener { emitter.onErrorSafe(it) }
        }
}