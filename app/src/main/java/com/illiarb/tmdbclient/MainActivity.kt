package com.illiarb.tmdbclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.illiarb.tmdbclient.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.common.BackPressedHandler
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var connectivityStatus: ConnectivityStatus

    override fun inject(appProvider: AppProvider) =
        MainComponent.get(appProvider, this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectivityStatus.connectionState()
            .onEach { updateConnectionStateLabel(it) }
            .launchIn(lifecycleScope)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        // TODO: Find a proper way of doing this
        val navHost = (nav_host_fragment as NavHostFragment)
        val handled = navHost.childFragmentManager.fragments.any {
            if (it is BackPressedHandler) {
                it.handleBackPress()
            } else {
                false
            }
        }

        if (!handled) {
            super.onBackPressed()
        }
    }

    private fun updateConnectionStateLabel(state: ConnectivityStatus.ConnectionState) {
        connectionStatusLabel.setVisible(state == ConnectivityStatus.ConnectionState.NOT_CONNECTED)
    }
}