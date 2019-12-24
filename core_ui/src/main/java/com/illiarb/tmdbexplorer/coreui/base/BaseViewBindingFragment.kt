package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.illiarb.tmdbexplorer.coreui.common.FragmentViewBinding

abstract class BaseViewBindingFragment<B : ViewBinding> : BaseFragment() {

    private val bindingWrapper by lazy(LazyThreadSafetyMode.NONE) {
        FragmentViewBinding(this) { getViewBinding(LayoutInflater.from(requireContext())) }
    }

    protected val binding: B
        get() = bindingWrapper.binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater): B

}