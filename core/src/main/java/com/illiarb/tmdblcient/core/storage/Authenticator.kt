package com.illiarb.tmdblcient.core.storage

import com.illiarb.tmdblcient.core.domain.entity.UserCredentials

/**
 * @author ilya-rb on 20.11.18.
 */
interface Authenticator {

    suspend fun authorize(credentials: UserCredentials)

    suspend fun isAuthenticated(): Boolean
}