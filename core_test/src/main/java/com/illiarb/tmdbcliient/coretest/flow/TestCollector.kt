package com.illiarb.tmdbcliient.coretest.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class TestCollector<T>(private val flow: Flow<T>) {

  private val values = mutableListOf<T>()

  suspend fun collect() = coroutineScope {
    flow.collect {
      values.add(it)
    }
  }

  fun current(): T = values.last()
}

fun <T> Flow<T>.test() = TestCollector(this)