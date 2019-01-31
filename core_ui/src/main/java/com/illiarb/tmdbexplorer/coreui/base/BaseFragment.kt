package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.illiarb.tmdbexplorer.coreui.uiactions.ShowErrorDialogAction
import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdbexplorer.coreui.uiactions.UiActionImpl
import com.illiarb.tmdbexplorer.coreui.uiactions.UiActions
import com.illiarb.tmdbexplorer.coreui.util.LawObserver
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
abstract class BaseFragment<T : BasePresentationModel<*>> : Fragment(),
    UiActions,
    Injectable,
    CoroutineScope {

    @Inject
    protected lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    protected lateinit var modelFactory: ViewModelProvider.Factory

    protected lateinit var presentationModel: T
        private set

    private val uiActions by lazy { UiActionImpl(requireActivity()) }

    private val actionsObserver: LawObserver<UiAction> by lazy(LazyThreadSafetyMode.NONE) {
        LawObserver.create(presentationModel.actionsObservable()) {
            handleAction(it)
        }
    }

    private val job = SupervisorJob()

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun getModelClass(): Class<T>

    override val coroutineContext: CoroutineContext
        get() = job + dispatcherProvider.main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        actionsObserver.register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    override fun showToast(message: String) = uiActions.showToast(message)

    override fun showErrorDialog(message: String) = uiActions.showErrorDialog(message)

    override fun showBlockingProgress() = uiActions.showBlockingProgress()

    override fun hideBlockingProgress() = uiActions.hideBlockingProgress()

    protected open fun handleAction(action: UiAction) {
        launch(context = coroutineContext) {
            when (action) {
                is ShowErrorDialogAction -> showErrorDialog(action.message)
            }
        }
    }

    protected inline fun runOnUi(crossinline block: () -> Unit) {
        launch(context = coroutineContext) {
            block()
        }
    }

    private fun createPresentationModel() {
        if (::modelFactory.isInitialized) {
            presentationModel = ViewModelProviders.of(this, modelFactory).get(getModelClass())
        }
    }
}