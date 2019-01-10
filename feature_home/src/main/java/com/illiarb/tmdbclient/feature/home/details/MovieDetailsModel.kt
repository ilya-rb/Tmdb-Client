package com.illiarb.tmdbclient.feature.home.details

import com.illiarb.tmdbclient.feature.home.domain.GetMovieDetails
import com.illiarb.tmdbexplorer.coreui.SimpleStateObservable
import com.illiarb.tmdbexplorer.coreui.StateObservable
import com.illiarb.tmdbexplorer.coreui.base.BaseViewModel
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class MovieDetailsModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getMovieDetails: GetMovieDetails
) : BaseViewModel() {

    private val stateObservable = SimpleStateObservable<MovieDetailsState>()

    override fun provideDefaultDispatcher(): CoroutineDispatcher = dispatcherProvider.mainDispatcher

    fun stateObservable(): StateObservable<MovieDetailsState> = stateObservable

    fun getMovieDetails(id: Int) = launch(context = coroutineContext) {
        stateObservable.accept(MovieDetailsState(true, null))

        try {
            val movie = getMovieDetails.execute(id)
            stateObservable.accept(MovieDetailsState(false, movie))
        } catch (e: Exception) {
            // Process error
        }
    }
}