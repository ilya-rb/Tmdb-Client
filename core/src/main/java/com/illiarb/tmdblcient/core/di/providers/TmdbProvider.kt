package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.services.TmdbService

interface TmdbProvider {

    fun provideTmdbService(): TmdbService
}