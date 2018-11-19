package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.illiarb.tmdbexplorer.coreui.actions.CommonUiActions
import com.illiarb.tmdbexplorer.coreui.actions.DefaultCommonUiActions
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), CommonUiActions {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val destroyDisposable = CompositeDisposable()

    protected lateinit var viewModel: T
        private set

    private val commonUiActions by lazy { DefaultCommonUiActions(this) }

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun getViewModelClass(): Class<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createViewModel()

        val contentView = getContentView()
        if (contentView != View.NO_ID) {
            setContentView(contentView)
        }
    }

    override fun onDestroy() {
        destroyDisposable.clear()
        super.onDestroy()
    }

    override fun showMessage(message: String) = commonUiActions.showMessage(message)

    override fun showError(message: String) = commonUiActions.showError(message)

    private fun createViewModel() {
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
        }
    }
}