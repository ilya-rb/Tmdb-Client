package com.illiarb.tmdblcient.core.navigation

interface NavigatorHolder {

    fun setNavigator(navigator: Navigator)

    fun removeNavigator()

    fun executeAction(action: Router.Action)
}