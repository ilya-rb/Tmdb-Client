package com.illiarb.tmdbclient.feature.explore.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.feature.explore.ExploreViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 31.10.18.
 */
@Module
interface ExploreModule {

    @Binds
    @IntoMap
    @ViewModelKey(ExploreViewModel::class)
    fun bindExploreViewModel(viewModel: ExploreViewModel): ViewModel

}