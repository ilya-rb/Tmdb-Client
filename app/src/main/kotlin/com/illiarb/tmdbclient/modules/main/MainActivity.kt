package com.illiarb.tmdbclient.modules.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ActivityMainBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.libs.ui.ext.isNightModeEnabled
import com.illiarb.tmdbclient.navigation.DeepLinkHandler
import com.illiarb.tmdbclient.navigation.Navigator
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.system.DayNightModeChangeNotifier
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
  lateinit var fragmentFactory: FragmentFactory

  @Inject
  lateinit var deepLinkHandler: DeepLinkHandler

  @Inject
  lateinit var dayNightModeChangeNotifier: DayNightModeChangeNotifier

  private val viewBinding: ActivityMainBinding by viewBinding(R.id.root)

  private var connectionSnackbar: Snackbar? = null

  override fun inject(appProvider: AppProvider) {
    DaggerMainComponent.factory()
      .create(activity = this, dependencies = appProvider)
      .inject(this)
  }

  @SuppressLint("UnsafeExperimentalUsageWarning")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    FragmentManager.enableNewStateManager(true)

    supportFragmentManager.registerFragmentLifecycleCallbacks(
      NavHostFragmentLifecycleCallbacks(
        fragmentFactory = fragmentFactory,
        onFactoryAttached = {
          supportFragmentManager.unregisterFragmentLifecycleCallbacks(it)
        }
      ),
      /* recursive */ false
    )

    setContentView(R.layout.activity_main)

    lifecycleScope.launch {
      connectivityStatus.connectionState().collect {
        updateConnectionStateLabel(it)
      }
    }

    maybeHandleDeepLink(intent)

    dayNightModeChangeNotifier.notifySystemNightModeChanged(isNightModeEnabled())
  }

  override fun onResumeFragments() {
    super.onResumeFragments()
    actionsBuffer.setNavigator(appNavigator)
  }

  override fun onPause() {
    super.onPause()
    actionsBuffer.removeNavigator()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    maybeHandleDeepLink(intent)
  }

  private fun maybeHandleDeepLink(intent: Intent?) {
    intent?.action?.let {
      deepLinkHandler.handleShortcut(it)
    }
  }

  private fun updateConnectionStateLabel(state: ConnectivityStatus.ConnectionState) {
    when (state) {
      ConnectivityStatus.ConnectionState.CONNECTED -> connectionSnackbar?.dismiss()
      ConnectivityStatus.ConnectionState.NOT_CONNECTED -> {
        connectionSnackbar = Snackbar.make(
          viewBinding.root,
          R.string.network_not_connected,
          Snackbar.LENGTH_INDEFINITE
        ).apply {
          @SuppressLint("NewApi")
          if (buildConfig.isQ) {
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