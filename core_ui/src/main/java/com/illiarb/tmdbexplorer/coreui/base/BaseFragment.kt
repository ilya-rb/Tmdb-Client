package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.illiarb.tmdbexplorer.coreui.uiactions.UiActionImpl
import com.illiarb.tmdbexplorer.coreui.uiactions.UiActions
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment(), UiActions {

    protected val destroyViewDisposable by lazy { CompositeDisposable() }

    private val uiActions by lazy { UiActionImpl(requireActivity()) }

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

    override fun showToast(message: String) = uiActions.showToast(message)

    override fun showErrorDialog(message: String) = uiActions.showErrorDialog(message)

    override fun showBlockingProgress() = uiActions.showBlockingProgress()

    override fun hideBlockingProgress() = uiActions.hideBlockingProgress()
}