package com.illiarb.tmdbclient.navigation

import androidx.navigation.Navigator
import androidx.navigation.fragment.CustomFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.illiarb.tmdbclient.R

/**
 * Custom nav host fragment that replaces default navigator
 * with a custom one:
 * @see FragmentNavigator
 * - added ability to use fragmentTransaction.add instead of replace
 */
class CustomNavHostFragment : NavHostFragment() {

  override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
    return CustomFragmentNavigator(requireContext(), childFragmentManager, R.id.nav_host_fragment)
  }
}