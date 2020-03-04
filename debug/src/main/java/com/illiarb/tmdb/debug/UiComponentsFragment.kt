package com.illiarb.tmdb.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.illiarb.tmdb.debug.databinding.FragmentUiComponentsBinding
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding

class UiComponentsFragment : Fragment() {

  companion object {

    const val TABS_COUNT = 3

    fun newInstance() = UiComponentsFragment()
  }

  private lateinit var binding: FragmentUiComponentsBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentUiComponentsBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.toolbar.doOnApplyWindowInsets { v, windowInsets, initialPadding ->
      v.updatePadding(top = initialPadding.top + windowInsets.systemWindowInsetTop)
    }

    for (i in 0 until TABS_COUNT) {
      binding.tabs.addTab(binding.tabs.newTab().setText("Tab $i"))
    }

    binding.nightMode.isChecked =
      AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

    binding.nightMode.setOnCheckedChangeListener { _, isChecked ->
      AppCompatDelegate.setDefaultNightMode(
        if (isChecked) {
          AppCompatDelegate.MODE_NIGHT_YES
        } else {
          AppCompatDelegate.MODE_NIGHT_NO
        }
      )
    }
  }
}