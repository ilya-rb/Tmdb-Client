package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.navigation.ShowMoviesListAction

interface StartScreenActionProvider {

    fun provideStartScreenAction(): ShowMoviesListAction

}