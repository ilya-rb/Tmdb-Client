package com.illiarb.tmdbclient.features.main

import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.navigation.Navigator
import javax.inject.Inject

/**
 * @author ilya-rb on 31.10.18.
 */
class MainViewModel @Inject constructor(
    private val navigator: Navigator
) : BaseViewModel() {

    fun onNavigationItemSelected(id: Int) {
        when (id) {
            R.id.moviesFragment -> navigator.showMoviesScreen()
            R.id.exploreFragment -> navigator.showExploreScreen()
        }
    }
}