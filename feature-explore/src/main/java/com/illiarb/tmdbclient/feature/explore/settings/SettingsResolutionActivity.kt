package com.illiarb.tmdbclient.feature.explore.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.android.gms.common.api.Status
import com.illiarb.tmdbexplorer.coreui.base.BaseActivity
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.system.EventBus

/**
 * @author ilya-rb on 01.11.18.
 */
class SettingsResolutionActivity : BaseActivity<BaseViewModel>(), Injectable {

    companion object {

        private const val EXTRA_STATUS = "status"
        private const val REQUEST_CODE_CHANGE_SETTINGS = 1000

        fun startSettingsResolution(context: Context, status: Status) {
            context.startActivity(
                Intent(context, SettingsResolutionActivity::class.java).putExtra(EXTRA_STATUS, status)
            )
        }
    }

    private lateinit var eventBus: EventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val status = intent.getParcelableExtra<Status>(EXTRA_STATUS)
        status.startResolutionForResult(this, REQUEST_CODE_CHANGE_SETTINGS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != REQUEST_CODE_CHANGE_SETTINGS) {
            return
        }

        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsApi
        when (requestCode) {
            Activity.RESULT_CANCELED -> Log.d(SettingsResolutionActivity::class.java.name, "Location setting change canceled")
            Activity.RESULT_OK -> {
                // All required changes were made
                Log.d(SettingsResolutionActivity::class.java.name, "Location settings changed")
            }
        }

        finish()
    }

    override fun getContentView(): Int = View.NO_ID

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun inject(appProvider: AppProvider) {
        // TODO Add normal di
        eventBus = appProvider.provideEventBus()
    }
}