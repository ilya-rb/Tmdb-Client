package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.illiarb.tmdbexplorer.coreui.actions.CommonUiActions
import com.illiarb.tmdbexplorer.coreui.actions.DefaultCommonUiActions
import io.reactivex.disposables.CompositeDisposable

abstract class BaseMviFragment : Fragment(), CommonUiActions {

    @Suppress("MemberVisibilityCanBePrivate")
    protected val destroyViewDisposable by lazy { CompositeDisposable() }

    private val commonUiActions by lazy { DefaultCommonUiActions(requireActivity()) }

    @LayoutRes
    protected abstract fun getContentView(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = getContentView()

        return if (contentView != View.NO_ID) {
            inflater.inflate(getContentView(), container, false)
        } else {
            null
        }
    }

    override fun onDestroyView() {
        destroyViewDisposable.clear()
        super.onDestroyView()
    }

    override fun showMessage(message: String) = commonUiActions.showMessage(message)

    override fun showError(message: String) = commonUiActions.showError(message)

    override fun showProgressDialog() = commonUiActions.showProgressDialog()

    override fun hideProgressDialog() = commonUiActions.hideProgressDialog()
}