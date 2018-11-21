package com.illiarb.tmdbclient.features.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.features.main.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseActivity
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), Injectable {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Navigation.findNavController(this, R.id.nav_host_fragment).addOnNavigatedListener(::onDestinationChanged)

        bottomNavigation.apply {
            setOnNavigationItemReselectedListener { /* No-op */ }
            setOnNavigationItemSelectedListener {
                viewModel.onBottomNavigationItemSelected(it.itemId)
                true
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun getContentView(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun inject(appProvider: AppProvider) = MainComponent.get(appProvider, this).inject(this)

    override fun onNavigateUp(): Boolean =
        Navigation
            .findNavController(this, R.id.nav_host_fragment)
            .navigateUp()

    private fun onDestinationChanged(
        @Suppress("UNUSED_PARAMETER") controller: NavController,
        destination: NavDestination
    ) {
        val menuItem = bottomNavigation.menu.findItem(destination.id)
        // Don't show bottom navigation
        // for non-root fragments
        bottomNavigation.visibility = if (menuItem == null && destination.id != R.id.authFragment) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}