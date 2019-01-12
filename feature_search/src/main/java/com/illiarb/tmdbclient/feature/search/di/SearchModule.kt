package com.illiarb.tmdbclient.feature.search.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.feature.search.presentation.SearchModel
import com.illiarb.tmdbexplorer.coreui.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
interface SearchModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchModel::class)
    fun bindSearchModel(impl: SearchModel): ViewModel
}