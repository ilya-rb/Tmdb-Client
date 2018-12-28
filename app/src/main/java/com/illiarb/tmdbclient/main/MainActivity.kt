package com.illiarb.tmdbclient.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.main.di.MainComponent
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.system.ConnectivityStatus
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var connectivityStatus: ConnectivityStatus

    private val destroyDisposable = CompositeDisposable()

    override fun inject(appProvider: AppProvider) = MainComponent.get(appProvider, this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        connectivityStatus.connectionState()
            .subscribe(::onConnectionStateChanged)
            .addTo(destroyDisposable)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onStop() {
        super.onStop()
        destroyDisposable.clear()
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