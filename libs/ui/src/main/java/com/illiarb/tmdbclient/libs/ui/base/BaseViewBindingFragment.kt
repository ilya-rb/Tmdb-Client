package com.illiarb.tmdbclient.libs.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingFragment<B : ViewBinding> : BaseFragment() {

  private var viewBinding: B? = null

  protected val binding: B
    get() = viewBinding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewBinding = getViewBinding(inflater)
    return viewBinding!!.root
  }

  override fun onDestroyView() {
    viewBinding = null
    super.onDestroyView()
  }

  abstract fun getViewBinding(inflater: LayoutInflater): B

}