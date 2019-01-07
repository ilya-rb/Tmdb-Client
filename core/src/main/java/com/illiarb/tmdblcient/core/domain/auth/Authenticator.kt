package com.illiarb.tmdblcient.core.domain.auth

/**
 * @author ilya-rb on 20.11.18.
 */
interface Authenticator {

    suspend fun authorize(username: String, password: String): Boolean

    suspend fun isAuthenticated(): Boolean
}