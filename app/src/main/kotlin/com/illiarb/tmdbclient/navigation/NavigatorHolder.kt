package com.illiarb.tmdbclient.navigation

import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject
import javax.inject.Singleton

interface NavigatorHolder {

  fun executeAction(action: Action)

  fun setNavigator(navigator: Navigator)

  fun removeNavigator()

  @Singleton
  class ActionsBuffer @Inject constructor() : NavigatorHolder {

    private val actionsBuffer: Queue<Action> = LinkedList()
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

    override fun executeAction(action: Action) {
      navigator?.executeAction(action) ?: actionsBuffer.add(action)
    }
  }
}