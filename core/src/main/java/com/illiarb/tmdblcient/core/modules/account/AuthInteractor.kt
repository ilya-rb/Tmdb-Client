package com.illiarb.tmdblcient.core.modules.account

/**
 * @author ilya-rb on 20.11.18.
 */
interface AuthInteractor {

    fun authorize(username: String, password: String)

}