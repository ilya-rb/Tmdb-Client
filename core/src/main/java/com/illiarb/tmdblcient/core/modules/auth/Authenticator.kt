package com.illiarb.tmdblcient.core.modules.auth

import io.reactivex.Completable

/**
 * @author ilya-rb on 20.11.18.
 */
interface Authenticator {

    fun authorize(username: String, password: String): Completable

    fun isAuthenticated(): Boolean
}