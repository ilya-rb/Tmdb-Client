package com.illiarb.tmdbclient.storage.network.api.service

import com.illiarb.tmdbclient.storage.model.AccountModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author ilya-rb on 21.11.18.
 */
interface AccountService {

    @GET("account")
    fun getAccountDetails(@Query("session_id") sessionId: String): Single<AccountModel>
}