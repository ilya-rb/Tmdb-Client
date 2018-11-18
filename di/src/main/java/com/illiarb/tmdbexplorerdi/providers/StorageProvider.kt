package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.storage.LocationRepository
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.illiarb.tmdblcient.core.system.ResourceResolver

/**
 * Interface for providing repositories
 * And all storage-module related dependencies
 */
interface StorageProvider {

    fun provideMoviesRepository(): MoviesRepository

    fun provideLocationRepository(): LocationRepository

    fun provideResourceResolver(): ResourceResolver
}