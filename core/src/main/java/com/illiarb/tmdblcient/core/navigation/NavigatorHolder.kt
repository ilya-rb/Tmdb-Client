package com.illiarb.tmdblcient.core.navigation

interface NavigatorHolder {

    fun executeAction(action: Router.Action)

    fun setNavigator(navigator: Navigator)

    fun removeNavigator()
}