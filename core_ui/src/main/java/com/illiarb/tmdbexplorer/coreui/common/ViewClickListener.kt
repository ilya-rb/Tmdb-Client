package com.illiarb.tmdbexplorer.coreui.common

import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdbexplorer.coreui.observable.BypassObservable
import com.illiarb.tmdbexplorer.coreui.observable.Observable
import com.illiarb.tmdbexplorer.coreui.observable.Observer
import javax.inject.Inject

/**
 * @author ilya-rb on 17.01.19.
 */
@FragmentScope
class ViewClickListener @Inject constructor() {

    private val clickObservable = BypassObservable<Any>()

    fun onClicked(payload: Any) {
        clickObservable.publish(payload)
    }

    fun observeClicks(observer: Observer<Any>) {
        clickObservable.addObserver(observer)
    }

    fun clickObservable(): Observable<Any> {
        return clickObservable
    }

    fun stopObserving(observer: Observer<Any>) {
        clickObservable.removeObserver(observer)
    }
}