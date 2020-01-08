package com.illiarb.tmdbcliient.coretest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.illiarb.tmdbcliient.coretest.rules.MainCoroutineRule
import com.illiarb.tmdbcliient.coretest.rules.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    protected fun runBlockingTest(block: suspend () -> Unit) {
        mainCoroutineRule.runBlocking {
            block()
        }
    }
}