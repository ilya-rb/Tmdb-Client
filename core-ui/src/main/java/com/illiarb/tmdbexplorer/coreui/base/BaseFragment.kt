package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.illiarb.tmdbexplorer.coreui.viewmodel.ViewModelScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment <T : ViewModel> : Fragment() {

    protected val destroyViewDisposable = CompositeDisposable()

    protected lateinit var viewModel: T
        private set

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun getViewModelClass(): Class<T>

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
        createViewModel()
    }

    override fun onDestroyView() {
        destroyViewDisposable.clear()
        super.onDestroyView()
    }

    private fun createViewModel() {
        if (::viewModelFactory.isInitialized) {
            viewModel = when (getViewModelScope()) {
                ViewModelScope.FRAGMENT ->
                    ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())

                ViewModelScope.ACTIVITY ->
                    ViewModelProviders.of(requireActivity(), viewModelFactory).get(getViewModelClass())
            }
        }
    }

    protected open fun getViewModelScope(): ViewModelScope = ViewModelScope.FRAGMENT
}