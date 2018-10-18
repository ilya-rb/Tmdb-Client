package com.illiarb.tmdbclient.features.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.features.main.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseActivity
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>(), Injectable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDestinationChangedListener(Navigation.findNavController(this, R.id.nav_host_fragment))
    }

    override fun getContentView(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun inject(appProvider: AppProvider) =
        MainComponent.get(appProvider, this).inject(this)

    override fun onNavigateUp(): Boolean =
        Navigation
            .findNavController(this, R.id.nav_host_fragment)
            .navigateUp()

    private fun addDestinationChangedListener(controller: NavController) {
        controller.addOnNavigatedListener { _, destination ->
            val menuItem = bottomNavigation.menu.findItem(destination.id)
            // Don't show bottom navigation
            // for non-root fragments
            bottomNavigation.visibility = if (menuItem == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}