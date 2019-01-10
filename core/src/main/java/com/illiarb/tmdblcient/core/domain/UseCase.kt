package com.illiarb.tmdblcient.core.domain

/**
 * @author ilya-rb on 09.01.19.
 */
interface UseCase<T, P> {

    suspend fun execute(payload: P): T
}