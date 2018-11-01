package com.illiarb.tmdbexplorer.coreui.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.illiarb.tmdbexplorer.coreui.base.BaseActivity
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.system.EventBus

/**
 * @author ilya-rb on 01.11.18.
 */
class PermissionsRequestActivity : BaseActivity<BaseViewModel>(), Injectable {

    companion object {

        const val REQUEST_CODE_PERMISSIONS = 1000
        const val EXTRA_PERMISSIONS = "permissions"

        fun startPermissionsRequest(context: Context, permissions: Array<out String>) {
            context.startActivity(
                Intent(context, PermissionsRequestActivity::class.java).putExtra(EXTRA_PERMISSIONS, permissions)
            )
        }
    }

    private lateinit var eventBus: EventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        requestPermissions(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != REQUEST_CODE_PERMISSIONS) {
            return
        }

        val permissionResults = permissions.mapIndexed { index: Int, permission: String ->
            val isGranted = grantResults[index] == PackageManager.PERMISSION_GRANTED
            PermissionResult(permission, isGranted)
        }

        eventBus.postEvent(PermissionResultList(permissionResults))

        finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        requestPermissions(intent)
    }

    override fun getContentView(): Int = View.NO_ID

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun inject(appProvider: AppProvider) {
        // TODO Add normal injection
        eventBus = appProvider.provideEventBus()
    }

    private fun requestPermissions(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS)
            requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
        }
    }
}