package com.illiarb.tmdbexplorer.coreui.uiactions

/**
 * @author ilya-rb on 19.11.18.
 */
interface UiActions {

    fun showToast(message: String)

    fun showErrorDialog(message: String)

    fun showBlockingProgress()

    fun hideBlockingProgress()

}