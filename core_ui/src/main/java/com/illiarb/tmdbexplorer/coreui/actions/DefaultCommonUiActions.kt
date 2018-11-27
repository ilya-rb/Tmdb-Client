package com.illiarb.tmdbexplorer.coreui.actions

import android.content.Context
import android.widget.Toast

/**
 * @author ilya-rb on 19.11.18.
 */
class DefaultCommonUiActions(val context: Context) : CommonUiActions {

    // TODO Add maybe more pretty dialogs

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}