package com.illiarb.tmdbclient.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.illiarb.tmdbclient.BuildConfig
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ActivityMainBinding
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.navigation.Navigator
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.ui.debug.DebugSelectorFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

  @Inject
  lateinit var appNavigator: Navigator

  @Inject
  lateinit var actionsBuffer: NavigatorHolder

  @Inject
  lateinit var connectivityStatus: ConnectivityStatus

  private lateinit var binding: ActivityMainBinding

  private var connectionSnackbar: Snackbar? = null

  override fun inject(appComponent: AppComponent) =
    DaggerMainComponent.builder()
      .activity(this)
      .dependencies(appComponent)
      .build()
      .inject(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if (BuildConfig.DEBUG) {
      binding.btnDebug.visibility = View.VISIBLE
      binding.btnDebug.setOnClickListener {
        DebugSelectorFragment.newInstance()
          .show(supportFragmentManager, DebugSelectorFragment::class.java.name)
      }
    }

    lifecycleScope.launch {
      connectivityStatus.connectionState().collect {
        updateConnectionStateLabel(it)
      }
    }
  }

  override fun onResumeFragments() {
    super.onResumeFragments()
    actionsBuffer.setNavigator(appNavigator)
  }

  override fun onPause() {
    super.onPause()
    actionsBuffer.removeNavigator()
  }

  private fun updateConnectionStateLabel(state: ConnectivityStatus.ConnectionState) {
    when (state) {
      ConnectivityStatus.ConnectionState.CONNECTED -> connectionSnackbar?.dismiss()
      ConnectivityStatus.ConnectionState.NOT_CONNECTED -> {
        connectionSnackbar = Snackbar.make(
          binding.root,
          R.string.network_not_connected,
          Snackbar.LENGTH_INDEFINITE
        ).apply {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setupSnackbarConnectivityAction()
          }
        }.also { it.show() }
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun Snackbar.setupSnackbarConnectivityAction() {
    setAction(R.string.network_not_connected_settings_panel) {
      startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    }
  }
}