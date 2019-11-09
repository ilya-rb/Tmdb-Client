package com.illiarb.tmdblcient.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

sealed class Either<out T : Any> {

    class Left<out T : Any>(val data: T) : Either<T>()

    class Right(val error: Throwable) : Either<Nothing>()

    fun getOrThrow(): T = when (this) {
        is Left -> data
        is Right -> throw error
    }

    companion object {

        suspend fun <T: Any> create(block: suspend () -> T): Either<T> {
            return try {
                Left(block())
            } catch (e: Exception) {
                Right(e)
            }
        }

        fun <T : Any> asFlow(block: suspend () -> T): Flow<Either<T>> =
            flow<Either<T>> { emit(Left(block())) }.catch { emit(Right(it)) }
    }
}