package com.illiarb.tmdbclient.navigation

import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Screen
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 31.01.19.
 */
@Singleton
class AppRouter @Inject constructor(holder: NavigatorHolder) : Router {

    // There always be 1 implementation
    private val navigatorHolder = holder as AppNavigatorHolder

    override fun navigateTo(screen: Screen) {
        navigatorHolder.runNavigation(screen)
    }
}