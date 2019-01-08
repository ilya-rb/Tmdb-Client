package com.illiarb.tmdblcient.core.domain.auth

import com.illiarb.tmdblcient.core.system.NonBlocking

/**
 * @author ilya-rb on 20.11.18.
 */
interface Authenticator {

    @NonBlocking
    suspend fun authorize(username: String, password: String): Boolean

    @NonBlocking
    suspend fun isAuthenticated(): Boolean
}