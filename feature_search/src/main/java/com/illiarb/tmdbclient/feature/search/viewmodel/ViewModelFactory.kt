package com.illiarb.tmdbclient.feature.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import javax.inject.Inject

/**
 * @author ilya-rb on 27.12.18.
 */
class ViewModelFactory @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(searchInteractor, schedulerProvider) as T
    }
}