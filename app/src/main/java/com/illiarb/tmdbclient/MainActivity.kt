package com.illiarb.tmdbclient

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.illiarb.tmdb.debug.DebugSelectorFragment
import com.illiarb.tmdbclient.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
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

        if (BuildConfig.DEBUG) {
            setupDebugView()
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
        connectionStatusLabel.setVisible(state == ConnectivityStatus.ConnectionState.NOT_CONNECTED)
    }

    private fun setupDebugView() {
        val btnDebug = findViewById<View>(R.id.btnDebug)

        btnDebug.visibility = View.VISIBLE

        btnDebug.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            view.updatePadding(top = initialPadding.top + windowInsets.systemWindowInsetTop)
        }

        btnDebug.setOnClickListener {
            DebugSelectorFragment.newInstance()
                .show(supportFragmentManager, DebugSelectorFragment::class.java.name)
        }
    }
}