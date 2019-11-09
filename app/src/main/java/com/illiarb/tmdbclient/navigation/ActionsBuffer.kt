package com.illiarb.tmdbclient.navigation

import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionsBuffer @Inject constructor() : NavigatorHolder {

    private val actionsBuffer: Queue<Router.Action> = LinkedList()
    private var navigator: Navigator? = null

    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator.also {
            while (actionsBuffer.isNotEmpty()) {
                actionsBuffer.poll()?.let {
                    executeAction(it)
                }
            }
        }
    }

    override fun removeNavigator() {
        navigator = null
    }

    override fun executeAction(action: Router.Action) {
        navigator?.executeAction(action) ?: actionsBuffer.add(action)
    }
}