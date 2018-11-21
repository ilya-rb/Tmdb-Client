package com.illiarb.tmdblcient.core.modules.auth

import io.reactivex.Completable

/**
 * @author ilya-rb on 21.11.18.
 */
interface AuthInteractor {

    fun authenticate(username: String, password: String): Completable

}