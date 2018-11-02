package com.illiarb.tmdbexplorer.coreui.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.system.EventBus
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 01.11.18.
 */
class PermissionResolver @Inject constructor(app: App, private val eventBus: EventBus) {

    private val context = app.getApplication()

    companion object {
        const val PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    fun requestPermissions(vararg permissions: String): Single<List<PermissionResult>> {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getSuccessPermissionsResult(permissions)
        } else {
            Single
                .fromCallable {
                    permissions.any {
                        context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
                    }
                }
                .flatMap { hasDeniesPermissions ->
                    if (hasDeniesPermissions) {
                        startPermissionsRequest(permissions)
                    } else {
                        getSuccessPermissionsResult(permissions)
                    }
                }
        }
    }

    private fun getSuccessPermissionsResult(permissions: Array<out String>): Single<List<PermissionResult>> =
        Flowable.fromArray(*permissions)
            .map { PermissionResult(it, true) }
            .toList()

    private fun startPermissionsRequest(permissions: Array<out String>): Single<List<PermissionResult>> {
        PermissionsRequestActivity.startPermissionsRequest(context, permissions)

        return eventBus.observeEvents(PermissionResultList::class.java)
            .firstOrError()
            .map { it.results }
    }
}