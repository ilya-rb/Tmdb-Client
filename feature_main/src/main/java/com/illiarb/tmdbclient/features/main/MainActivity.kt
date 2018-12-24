package com.illiarb.tmdbclient.features.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.features.main.di.MainComponent
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MoviesScreen
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authenticator: Authenticator

    override fun inject(appProvider: AppProvider) = MainComponent.get(appProvider, this).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigation.findNavController(this, R.id.nav_host_fragment).addOnNavigatedListener(::onDestinationChanged)

        bottomNavigation.apply {
            setOnNavigationItemReselectedListener { /* No-op */ }
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.moviesFragment -> router.navigateTo(MoviesScreen)
                    R.id.accountFragment -> {
                        if (authenticator.isAuthenticated()) {
                            router.navigateTo(AccountScreen)
                        } else {
                            router.navigateTo(AuthScreen)
                        }
                    }
                }
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