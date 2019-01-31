package com.illiarb.tmdblcient.core.storage

import com.illiarb.tmdblcient.core.domain.entity.UserCredentials
import com.illiarb.tmdblcient.core.common.NonBlocking

/**
 * @author ilya-rb on 20.11.18.
 */
interface Authenticator {

    @NonBlocking
    suspend fun authorize(credentials: UserCredentials)

    @NonBlocking
    suspend fun isAuthenticated(): Boolean
}