package com.illiarb.tmdbclient.navigation

import javax.inject.Inject

class DeepLinkHandler @Inject constructor(
  private val router: Router
) {

  fun handleShortcut(action: String) {
    if (action == AppShortcuts.ACTION_DISCOVER) {
      router.executeAction(NavigationAction.DeepLink.Discover)
    }
  }
}