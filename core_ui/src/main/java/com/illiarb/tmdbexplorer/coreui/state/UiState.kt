package com.illiarb.tmdbexplorer.coreui.state

class UiState<T>(
    private val data: T? = null,
    private val error: Throwable? = null,
    private val isLoading: Boolean = false
) {

    companion object {

        fun <T> createLoadingState(): UiState<T> = UiState(isLoading = true)

        fun <T> createErrorState(error: Throwable) = UiState<T>(error = error)

        fun <T> createSuccessState(data: T) = UiState(data)
    }

    fun hasError(): Boolean = error != null

    fun hasData(): Boolean = data != null

    fun isLoading(): Boolean = isLoading

    fun requireData(): T = data ?: throw IllegalStateException("Data is null")

    fun requireError(): Throwable = error ?: throw IllegalStateException("Error is null")
}