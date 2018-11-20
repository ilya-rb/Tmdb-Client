package com.illiarb.tmdblcient.core.modules.main

import com.illiarb.tmdblcient.core.navigation.ScreenName

/**
 * @author ilya-rb on 20.11.18.
 */
interface MainInteractor {

    fun onMainScreenSelected(screenName: ScreenName)

    fun onAccountSelected()
}