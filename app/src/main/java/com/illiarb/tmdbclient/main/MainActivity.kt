package com.illiarb.tmdbclient.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.main.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var connectivityStatus: ConnectivityStatus

    private var connectivityStatusJob: Job? = null

    override fun inject(appProvider: AppProvider) = MainComponent.get(appProvider, this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)

        connectivityStatusJob = lifecycleScope.launch {
            connectivityStatus.setCoroutineScope(lifecycleScope)
            connectivityStatus.connectionState().collect {
                updateConnectionStateLabel(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()

        connectivityStatusJob?.cancel()
        connectivityStatus.removeCoroutineScope()
    }

    private fun updateConnectionStateLabel(state: ConnectivityStatus.ConnectionState) {
        connectionStatusLabel.setVisible(state == ConnectivityStatus.ConnectionState.NOT_CONNECTED)
    }
}