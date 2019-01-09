package com.illiarb.tmdbclient.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.main.di.MainComponent
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.system.ConnectivityStatus
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), Injectable, CoroutineScope {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private val coroutineJob = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutineJob + dispatcherProvider.mainDispatcher

    override fun inject(appProvider: AppProvider) = MainComponent.get(appProvider, this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onNavigateUp(): Boolean =
        Navigation
            .findNavController(this, R.id.nav_host_fragment)
            .navigateUp()

    private fun onConnectionStateChanged(state: ConnectivityStatus.ConnectionState) =
        when (state) {
            ConnectivityStatus.ConnectionState.CONNECTED -> connectionStatusLabel.visibility = View.GONE
            ConnectivityStatus.ConnectionState.NOT_CONNECTED -> connectionStatusLabel.visibility = View.VISIBLE
        }
}