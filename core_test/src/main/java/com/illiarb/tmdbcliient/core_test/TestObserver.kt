package com.illiarb.tmdbcliient.core_test

import com.illiarb.tmdblcient.core.util.observable.Observer
import org.junit.Assert.assertEquals

/**
 * @author ilya-rb on 24.01.19.
 */
class TestObserver<T> : Observer<T> {

    private val values = mutableListOf<T>()

    override fun onNewValue(value: T) {
        values.add(value)
    }

    fun assertValuesCount(count: Int): TestObserver<T> {
        assertEquals(count, values.size)
        return this
    }

    fun assertNoValues(): TestObserver<T> {
        assertEquals(true, values.isEmpty())
        return this
    }

    fun clearValues(): TestObserver<T> {
        values.clear()
        return this
    }

    fun withPrevious(block: (T) -> Unit): TestObserver<T> {
        block(values[values.size - 2])
        return this
    }

    fun withLatest(block: (T) -> Unit): TestObserver<T> {
        block(values.last())
        return this
    }
}