package com.illiarb.tmdbexplorer.coreui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.illiarb.tmdbexplorer.coreui.R

/**
 * @author ilya-rb on 28.11.18.
 */
class ProgressDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_progress_dialog, container, false)

    override fun onResume() {
        super.onResume()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, ProgressDialog::class.java.name)
    }
}