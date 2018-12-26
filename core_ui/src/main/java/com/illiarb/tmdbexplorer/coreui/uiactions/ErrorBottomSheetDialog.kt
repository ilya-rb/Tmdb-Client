package com.illiarb.tmdbexplorer.coreui.uiactions

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illiarb.tmdbexplorer.coreui.R
import kotlinx.android.synthetic.main.fragment_error_bottom_sheet_dialog.*

/**
 * @author ilya-rb on 26.12.18.
 */
class ErrorBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {

        private const val ARG_ERROR_MESSAGE = "message"

        fun newInstance(message: String): ErrorBottomSheetDialog =
            ErrorBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_ERROR_MESSAGE, message)
                }
            }
    }

    private var dialogDismissListener: DialogDismissListener? = null

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_error_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            errorMessage.text = it.getString(ARG_ERROR_MESSAGE)
        }

        errorBtnDismiss.setOnClickListener {
            dismiss()
            dialogDismissListener?.onDialogDismissed()
        }
    }

    override fun setTargetFragment(fragment: Fragment?, requestCode: Int) {
        super.setTargetFragment(fragment, requestCode)

        if (fragment is DialogDismissListener) {
            dialogDismissListener = fragment
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, ErrorBottomSheetDialog::class.java.name)
    }

    interface DialogDismissListener {

        fun onDialogDismissed()
    }
}