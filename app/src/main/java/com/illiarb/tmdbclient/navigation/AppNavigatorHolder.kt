package com.illiarb.tmdbclient.navigation

import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Screen
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 31.01.19.
 */
@Singleton
class AppNavigatorHolder @Inject constructor() : NavigatorHolder {

    private val screensBuffer: Queue<Screen> = LinkedList<Screen>()

    private var navigator: Navigator? = null

    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator

        while (screensBuffer.isNotEmpty()) {
            this.navigator?.runNavigation(screensBuffer.poll()) ?: break
        }
    }

    override fun removeNavigator() {
        navigator = null
    }

    fun runNavigation(screen: Screen) {
        if (navigator == null) {
            screensBuffer.add(screen)
        } else {
            navigator?.runNavigation(screen)
        }
    }
}