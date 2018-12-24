package com.illiarb.tmdblcient.core.di

import com.illiarb.tmdblcient.core.di.providers.AppProvider

/**
 * @author ilya-rb on 24.12.18.
 */
interface Injectable {

    fun inject(appProvider: AppProvider)
}