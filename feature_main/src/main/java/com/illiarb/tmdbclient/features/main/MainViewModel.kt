package com.illiarb.tmdbclient.features.main

import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.modules.main.MainInteractor
import com.illiarb.tmdblcient.core.navigation.ScreenName
import javax.inject.Inject

/**
 * @author ilya-rb on 31.10.18.
 */
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor
) : BaseViewModel() {

    fun onBottomNavigationItemSelected(id: Int) {
        mainInteractor.onMainScreenSelected(mapToScreenName(id))
    }

    private fun mapToScreenName(id: Int): ScreenName =
        when (id) {
            R.id.moviesFragment -> ScreenName.MOVIES
            R.id.exploreFragment -> ScreenName.EXPLORE
            R.id.accountFragment -> ScreenName.ACCOUNT
            else -> TODO("Implement other screens")
        }
}