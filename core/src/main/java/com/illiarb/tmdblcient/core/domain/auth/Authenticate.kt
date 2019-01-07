package com.illiarb.tmdblcient.core.domain.auth

import com.illiarb.tmdblcient.core.system.NonBlocking

/**
 * @author ilya-rb on 07.01.19.
 */
interface Authenticate {

    @NonBlocking
    suspend operator fun invoke(username: String, password: String): Boolean
}