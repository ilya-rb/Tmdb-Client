package com.illiarb.tmdbclient.feature.home.list

import com.illiarb.tmdbexplorer.coreui.base.BaseViewModel
import com.illiarb.tmdblcient.core.domain.movie.GetAllMovies
import com.illiarb.tmdblcient.core.domain.movie.GetMovieFilters
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getAllMovies: GetAllMovies,
    private val getMovieFilters: GetMovieFilters
) : BaseViewModel() {

    init {

    }

    override fun provideDefaultDispatcher(): CoroutineDispatcher = dispatcherProvider.mainDispatcher
}