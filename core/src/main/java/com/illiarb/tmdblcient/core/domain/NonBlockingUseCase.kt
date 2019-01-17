package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking

/**
 * @author ilya-rb on 09.01.19.
 */
interface NonBlockingUseCase<T, P> {

    @NonBlocking
    suspend fun executeAsync(payload: P): Result<T>
}