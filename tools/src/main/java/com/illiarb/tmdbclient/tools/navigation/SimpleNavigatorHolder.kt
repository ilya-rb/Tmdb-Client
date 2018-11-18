package com.illiarb.tmdbclient.tools.navigation

import com.illiarb.tmdblcient.core.navigation.NavigationData
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class SimpleNavigatorHolder @Inject constructor() : NavigatorHolder {

    private var navigator: Navigator? = null

    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
    }

    override fun removeNavigator() {
        this.navigator = null
    }

    fun runNavigation(data: NavigationData) {
        navigator?.runNavigate(data)
    }
}