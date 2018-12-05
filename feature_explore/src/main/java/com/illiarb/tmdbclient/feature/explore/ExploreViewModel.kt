package com.illiarb.tmdbclient.feature.explore

import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.modules.explore.ExploreInteractor
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreViewModel @Inject constructor(
    private val exploreInteractor: ExploreInteractor
) : BaseViewModel(), CoroutineScope {

    private val coroutinesJob = Job()
    private val theatersSubject = BehaviorSubject.create<UiState<List<Location>>>()

    override val coroutineContext: CoroutineContext
        get() = coroutinesJob + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        coroutinesJob.cancel()
    }

    fun observeNearbyTheaters(): Observable<UiState<List<Location>>> = theatersSubject.hide()

    fun fetchNearbyMovieTheaters() {
        launch(context = coroutineContext) {
            try {
                theatersSubject.onNext(UiState.createLoadingState())
                val result = withContext(Dispatchers.IO) {
                    getNearbyLocationInterop()
                }
                theatersSubject.onNext(UiState.createSuccessState(result))
            } catch (e: Exception) {
                theatersSubject.onNext(UiState.createErrorState(e))
            }
        }
    }

    // For now keep coroutine together with rxJava
    // Just to use coroutines in single module
    private suspend fun getNearbyLocationInterop(): List<Location> =
        suspendCoroutine { c ->
            exploreInteractor.getNearbyMovieTheaters()
                .subscribe(
                    { c.resume(it) },
                    { c.resumeWithException(it) }
                )
                .addTo(clearDisposable)
        }
}