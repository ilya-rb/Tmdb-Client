package com.illiarb.tmdbexplorer.coreui.common

import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdblcient.core.util.observable.Observer
import com.illiarb.tmdblcient.core.util.observable.SimpleObservable
import javax.inject.Inject

/**
 * @author ilya-rb on 17.01.19.
 */
@FragmentScope
class ViewClickListener @Inject constructor() {

    private val clickObservable =
        SimpleObservable<Any>()

    fun clickObservable(): SimpleObservable<Any> = clickObservable

    fun onClicked(payload: Any) {
        clickObservable.accept(payload)
    }

    fun observeClicks(observer: Observer<Any>) {
        clickObservable.addObserver(observer)
    }

    fun stopObserving(observer: Observer<Any>) {
        clickObservable.removeObserver(observer)
    }
}