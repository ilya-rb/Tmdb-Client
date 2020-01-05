package com.illiarb.tmdbclient.discover.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.discover.DefaultDiscoverModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class DiscoverModule(private val genreId: Int) {

    @Provides
    @IntoMap
    @ViewModelKey(DefaultDiscoverModel::class)
    fun provideDiscoverModel(
        tmdbService: TmdbService,
        router: Router,
        analyticsService: AnalyticsService
    ): ViewModel {
        return DefaultDiscoverModel(
            genreId,
            tmdbService,
            router,
            analyticsService
        )
    }
}