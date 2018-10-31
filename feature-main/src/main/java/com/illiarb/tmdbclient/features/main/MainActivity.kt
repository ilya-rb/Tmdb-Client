package com.illiarb.tmdbclient.features.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.illiarb.tmdbclient.features.main.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseActivity
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.system.EventBus
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<BaseViewModel>(), Injectable {

    @Inject
    lateinit var eventBus: EventBus

    @Inject
    @JvmField
    var appNavigationGraphRes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val host = NavHostFragment.create(appNavigationGraphRes)

            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_container, host)
                .setPrimaryNavigationFragment(host)
                .commitNow()

            addDestinationChangedListener(host.navController)
        }

        eventBus.observeEvents(NavDirections::class.java)
            .subscribe { Navigation.findNavController(this, R.id.nav_host_container).navigate(it) }
            .addTo(destroyDisposable)
    }

    override fun getContentView(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun inject(appProvider: AppProvider) = MainComponent.get(appProvider).inject(this)

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