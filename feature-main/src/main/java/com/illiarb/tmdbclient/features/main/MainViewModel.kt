package com.illiarb.tmdbclient.features.main

import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.navigation.ShowMoviesListAction
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val showMoviesListAction: ShowMoviesListAction
) : BaseViewModel() {

    fun onScreenInitialized() {
        showMoviesListAction.showStartScreen()
    }
}