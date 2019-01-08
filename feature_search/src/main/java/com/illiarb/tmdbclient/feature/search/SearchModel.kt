package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbexplorer.coreui.base.BaseViewModel
import com.illiarb.tmdblcient.core.domain.movie.SearchMovies
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class SearchModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val searchMovies: SearchMovies
) : BaseViewModel() {

    override fun provideDefaultDispatcher(): CoroutineDispatcher = dispatcherProvider.mainDispatcher
}