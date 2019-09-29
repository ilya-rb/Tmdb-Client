package com.illiarb.tmdbcliient.core_test.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.illiarb.tmdblcient.core.util.CoroutineScopeFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testErrorHandler = TestCoroutineExceptionHandler()
    private val testScope = CoroutineScope(testDispatcher + testErrorHandler)

    @Before
    fun setup() {
        CoroutineScopeFactory.setCustomViewModelScope(testScope)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        CoroutineScopeFactory.resetCustomViewModelScope()
        Dispatchers.resetMain()
    }
}