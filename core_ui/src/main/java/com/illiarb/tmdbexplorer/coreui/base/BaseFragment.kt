package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.illiarb.tmdbexplorer.coreui.uiactions.UiActionImpl
import com.illiarb.tmdbexplorer.coreui.uiactions.UiActions
import com.illiarb.tmdblcient.core.di.Injectable
import javax.inject.Inject

abstract class BaseFragment<T : BasePresentationModel<*>> : Fragment(), UiActions, Injectable {

    private val uiActions by lazy { UiActionImpl(requireActivity()) }

    protected lateinit var presentationModel: T
        private set

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun getModelClass(): Class<T>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = getContentView()

        return if (contentView != View.NO_ID) {
            inflater.inflate(getContentView(), container, false)
        } else {
            null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createPresentationModel()
    }

    override fun showToast(message: String) = uiActions.showToast(message)

    override fun showErrorDialog(message: String) = uiActions.showErrorDialog(message)

    override fun showBlockingProgress() = uiActions.showBlockingProgress()

    override fun hideBlockingProgress() = uiActions.hideBlockingProgress()

    private fun createPresentationModel() {
        if (::modelFactory.isInitialized) {
            presentationModel = ViewModelProviders.of(this, modelFactory).get(getModelClass())
        }
    }
}