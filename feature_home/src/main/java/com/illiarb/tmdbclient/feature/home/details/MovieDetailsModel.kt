package com.illiarb.tmdbclient.feature.home.details

import com.illiarb.tmdbexplorer.coreui.base.BaseViewModel
import com.illiarb.tmdblcient.core.domain.movie.GetMovieDetails
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class MovieDetailsModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getMovieDetails: GetMovieDetails
) : BaseViewModel() {

    override fun provideDefaultDispatcher(): CoroutineDispatcher = dispatcherProvider.mainDispatcher
}