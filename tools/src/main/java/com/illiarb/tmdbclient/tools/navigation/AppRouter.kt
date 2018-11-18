package com.illiarb.tmdbclient.tools.navigation

import com.illiarb.tmdblcient.core.navigation.NavigationData
import com.illiarb.tmdblcient.core.navigation.Router
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class AppRouter @Inject constructor(private val navigatorHolder: SimpleNavigatorHolder) : Router {

    override fun navigateTo(data: NavigationData) {
        navigatorHolder.runNavigation(data)
    }
}