package com.illiarb.tmdblcient.core.storage

/**
 * @author ilya-rb on 20.11.18.
 */
interface AccountRepository {

    fun isAuthorized(): Boolean

}