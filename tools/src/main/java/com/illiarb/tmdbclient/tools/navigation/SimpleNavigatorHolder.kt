package com.illiarb.tmdbclient.tools.navigation

import com.illiarb.tmdblcient.core.navigation.NavigationData
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class SimpleNavigatorHolder @Inject constructor() : NavigatorHolder {

    private val navEventsBuffer: Queue<NavigationData> = LinkedList<NavigationData>()

    private var navigator: Navigator? = null

    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator

        while (navEventsBuffer.isNotEmpty()) {
            this.navigator?.runNavigate(navEventsBuffer.poll()) ?: break
        }
    }

    override fun removeNavigator() {
        this.navigator = null
    }

    fun runNavigation(data: NavigationData) {
        if (navigator == null) {
            navEventsBuffer.add(data)
        } else {
            navigator?.runNavigate(data)
        }
    }
}