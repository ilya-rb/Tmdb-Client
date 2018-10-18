package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val destroyDisposable = CompositeDisposable()

    protected lateinit var viewModel: T
        private set

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

    private fun createViewModel() {
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
        }
    }
}