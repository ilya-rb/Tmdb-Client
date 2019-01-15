package com.illiarb.tmdbclient.tools.navigation

import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.ScreenData
import java.util.*
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class SimpleNavigatorHolder @Inject constructor() : NavigatorHolder {

    private val navEventsBuffer: Queue<ScreenData> = LinkedList<ScreenData>()

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

    fun runNavigation(data: ScreenData) {
        if (navigator == null) {
            navEventsBuffer.add(data)
        } else {
            navigator?.runNavigate(data)
        }
    }
}