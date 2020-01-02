package com.illiarb.tmdbclient

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.illiarb.tmdb.debug.DebugSelectorFragment
import com.illiarb.tmdbclient.databinding.ActivityMainBinding
import com.illiarb.tmdbclient.di.MainComponent
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus.ConnectionState
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

    override fun inject(appProvider: AppProvider) =
        MainComponent.get(appProvider, this).inject(this)

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

    private fun updateConnectionStateLabel(state: ConnectionState) {
        when (state) {
            ConnectionState.CONNECTED -> connectionSnackbar?.dismiss()
            ConnectionState.NOT_CONNECTED -> {
                connectionSnackbar = Snackbar.make(
                    binding.root,
                    R.string.network_not_connected,
                    Snackbar.LENGTH_INDEFINITE
                ).apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        setupSnackbarConnectivityAction()
                    }
                }
                connectionSnackbar!!.show()
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