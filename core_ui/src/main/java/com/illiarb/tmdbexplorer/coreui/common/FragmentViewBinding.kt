package com.illiarb.tmdbexplorer.coreui.common

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding

class FragmentViewBinding<B : ViewBinding>(
    private val fragment: Fragment,
    private var viewBindingCreator: (LayoutInflater) -> B
) : LifecycleObserver {

    private var _binding: B? = null

    val binding: B
        get() {
            if (_binding == null) {
                _binding = viewBindingCreator(LayoutInflater.from(fragment.requireContext()))
            }
            return _binding!!
        }

    init {
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        _binding = null
    }
}