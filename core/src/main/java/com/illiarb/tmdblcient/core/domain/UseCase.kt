package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.system.NonBlocking

/**
 * @author ilya-rb on 09.01.19.
 */
interface UseCase<T, P> {

    @NonBlocking
    suspend fun execute(payload: P): T
}