package com.illiarb.tmdbcliient.core_test

import com.illiarb.tmdblcient.core.util.observable.Observable

/**
 * @author ilya-rb on 26.01.19.
 */
inline fun <T> runWithSubscription(
    observable: Observable<T>,
    block: (TestObserver<T>) -> Unit
) {
    val observer = TestObserver<T>()

    observable.addObserver(observer)
    block(observer)
    observable.removeObserver(observer)
}