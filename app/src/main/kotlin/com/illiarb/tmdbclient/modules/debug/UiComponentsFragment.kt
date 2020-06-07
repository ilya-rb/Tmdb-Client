package com.illiarb.tmdbclient.modules.debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentUiComponentsBinding
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding

class UiComponentsFragment : BaseFragment(R.layout.fragment_ui_components) {

  companion object {
    private const val TABS_COUNT = 3
  }

  private val viewBinding by viewBinding { fragment ->
    FragmentUiComponentsBinding.bind(fragment.requireView())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewBinding.toolbar.doOnApplyWindowInsets { v, windowInsets, initialPadding ->
      v.updatePadding(top = initialPadding.top + windowInsets.systemWindowInsetTop)
    }

    for (i in 0 until TABS_COUNT) {
      viewBinding.tabs.addTab(viewBinding.tabs.newTab().setText("Tab $i"))
    }

    viewBinding.nightMode.isChecked =
      AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

    viewBinding.nightMode.setOnCheckedChangeListener { _, isChecked ->
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