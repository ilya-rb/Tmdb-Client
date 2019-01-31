package com.illiarb.tmdbclient.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.main.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdbexplorer.coreui.keyboard.KeyboardContentResizer
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.system.ConnectivityStatus
import com.illiarb.tmdblcient.core.system.ConnectivityStatus.ConnectionState
import com.illiarb.tmdblcient.core.util.observable.Observer
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable, Observer<ConnectionState> {

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

        KeyboardContentResizer.listen(this)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
        connectivityStatus.connectionState().addObserver(this)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
        connectivityStatus.connectionState().removeObserver(this)
    }

    override fun onNavigateUp(): Boolean =
        Navigation
            .findNavController(this, R.id.nav_host_fragment)
            .navigateUp()

    override fun onNewValue(value: ConnectionState) {
        connectionStatusLabel.setVisible(value == ConnectionState.NOT_CONNECTED)
    }
}