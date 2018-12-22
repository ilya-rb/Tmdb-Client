package com.illiarb.tmdbexplorer.coreui.actions

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbexplorer.coreui.common.ProgressDialog

/**
 * @author ilya-rb on 19.11.18.
 */
class DefaultCommonUiActions(val activity: FragmentActivity) : CommonUiActions {

    private val progressDialog by lazy { ProgressDialog() }

    override fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showProgressDialog() {
        if (!progressDialog.isAdded) {
            progressDialog.show(activity.supportFragmentManager)
        }
    }

    override fun hideProgressDialog() {
        if (progressDialog.isAdded) {
            progressDialog.dismiss()
        }
    }
}