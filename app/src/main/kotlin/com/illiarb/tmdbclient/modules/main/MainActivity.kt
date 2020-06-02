package com.illiarb.tmdbclient.modules.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ActivityMainBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.navigation.Navigator
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.navigation.Router
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

  @Inject
  lateinit var buildConfig: BuildConfig

  @Inject
  lateinit var router: Router

  private lateinit var binding: ActivityMainBinding

  private var connectionSnackbar: Snackbar? = null

  override fun inject(appProvider: AppProvider) =
    DaggerMainComponent.builder()
      .activity(this)
      .dependencies(appProvider)
      .build()
      .inject(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

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