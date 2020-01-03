package com.illiarb.tmdbclient.storage.error

import com.google.gson.JsonParser
import com.illiarb.tmdblcient.core.exception.ApiException
import com.illiarb.tmdblcient.core.exception.NetworkException
import com.illiarb.tmdblcient.core.exception.UnknownErrorException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * @author ilya-rb on 16.01.19.
 */
class ErrorHandler @Inject constructor(private val errorMessageBag: ErrorMessageBag) {

    companion object {
        private const val API_ERROR_STATUS_CODE = "status_code"
        private const val API_ERROR_STATUS_MESSAGE = "status_message"
    }

    private val jsonParser: JsonParser = JsonParser()

    fun createExceptionFromThrowable(error: Throwable): Throwable =
        when (error) {
            is SocketTimeoutException -> NetworkException(errorMessageBag.getNetworkConnectionMessage())
            is HttpException -> createNetworkError(error)
            else -> UnknownErrorException(errorMessageBag.getDefaultErrorMessage())
        }

    private fun createNetworkError(error: HttpException): Throwable {
        val errorBody = error.response()?.errorBody()
        if (errorBody != null) {
            val root = jsonParser.parse(errorBody.string()).asJsonObject

            val errorCode = root.get(API_ERROR_STATUS_CODE)
            val errorMessage = root.get(API_ERROR_STATUS_MESSAGE)

            if (errorCode != null && errorMessage != null) {
                return ApiException(errorCode.asInt, errorMessage.asString)
            }
        }
        return UnknownErrorException(errorMessageBag.getDefaultErrorMessage())
    }
}