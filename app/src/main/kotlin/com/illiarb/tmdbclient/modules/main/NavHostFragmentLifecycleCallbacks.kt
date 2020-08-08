package com.illiarb.tmdbclient.modules.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment

class NavHostFragmentLifecycleCallbacks(
  private val fragmentFactory: FragmentFactory,
  private val onFactoryAttached: (FragmentManager.FragmentLifecycleCallbacks) -> Unit
) : FragmentManager.FragmentLifecycleCallbacks() {

  override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
    super.onFragmentPreAttached(fm, f, context)

    if (f is NavHostFragment) {
      f.childFragmentManager.fragmentFactory = fragmentFactory
      onFactoryAttached(this)
    }
  }
}