package com.illiarb.tmdbexplorerdi

import com.illiarb.tmdbexplorerdi.providers.AppProvider

interface Injectable {

    fun inject(appProvider: AppProvider)

}