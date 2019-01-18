package com.illiarb.tmdbexplorer.coreui.uiactions

import android.widget.Toast
import androidx.fragment.app.FragmentActivity

/**
 * @author ilya-rb on 19.11.18.
 */
class UiActionImpl(val activity: FragmentActivity) : UiActions {

    private val blockingProgressDialog by lazy(LazyThreadSafetyMode.NONE) {
        BlockingProgressDialog()
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showErrorDialog(message: String) {
        ErrorBottomSheetDialog.newInstance(message).show(activity.supportFragmentManager)
    }

    override fun showBlockingProgress() {
        if (!blockingProgressDialog.isAdded) {
            blockingProgressDialog.show(activity.supportFragmentManager)
        }
    }

    override fun hideBlockingProgress() {
        if (blockingProgressDialog.isVisible) {
            blockingProgressDialog.dismiss()
        }
    }
}