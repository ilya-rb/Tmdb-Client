package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    @LayoutRes
    protected abstract fun getContentView(): Int

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
}