package com.illiarb.tmdbclient.storage.network.api.service

import com.illiarb.tmdbclient.storage.model.AccountModel
import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.model.ResultsModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author ilya-rb on 21.11.18.
 */
interface AccountService {

    @GET("account")
    fun getAccountDetailsAsync(@Query("session_id") sessionId: String): Deferred<AccountModel>

    @GET("account/{account_id}/favorite/movies")
    fun getAccountFavoriteMoviesAsync(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String
    ): Deferred<ResultsModel<MovieModel>>

    @GET("account/{account_id}/rated/movies")
    fun getAccountRatedMoviesAsync(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String
    ): Deferred<ResultsModel<MovieModel>>
}