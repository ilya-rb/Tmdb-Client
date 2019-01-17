package com.illiarb.tmdblcient.core.auth

import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking

/**
 * @author ilya-rb on 20.11.18.
 */
interface Authenticator {

    @NonBlocking
    suspend fun authorize(credentials: UserCredentials)

    @NonBlocking
    suspend fun isAuthenticated(): Boolean
}