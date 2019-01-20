package com.illiarb.tmdblcient.core.util.observable

/**
 * @author ilya-rb on 20.01.19.
 */
interface Observer <T> {

    fun onNewValue(value: T)
}