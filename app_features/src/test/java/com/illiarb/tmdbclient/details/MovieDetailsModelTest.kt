package com.illiarb.tmdbclient.details

import com.illiarb.tmdbcliient.core_test.TestDependencyProvider
import com.illiarb.tmdbcliient.core_test.viewmodel.BaseViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MovieDetailsModelTest : BaseViewModelTest() {

    private val viewModel: MovieDetailsViewModel by lazy {
        MovieDetailsViewModel.DefaultDetailsViewModel(
            TestDependencyProvider.provideTmdbService(),
            1
        )
    }
}