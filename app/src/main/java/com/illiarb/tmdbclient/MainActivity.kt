package com.illiarb.tmdbclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.illiarb.tmdbclient.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import kotlinx.android.synthetic.main.activity_main.*
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

    override fun inject(appProvider: AppProvider) =
        MainComponent.get(appProvider, this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        connectionStatusLabel.setVisible(state == ConnectivityStatus.ConnectionState.NOT_CONNECTED)
    }
}