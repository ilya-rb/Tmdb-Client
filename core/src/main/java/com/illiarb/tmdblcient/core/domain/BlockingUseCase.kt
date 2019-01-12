package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.system.Blocking

/**
 * @author ilya-rb on 11.01.19.
 */
interface BlockingUseCase<T, P> {

    @Blocking
    fun executeBlocking(payload: P): T
}