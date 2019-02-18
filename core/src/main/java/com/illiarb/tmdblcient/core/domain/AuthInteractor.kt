package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.entity.UserCredentials

/**
 * @author ilya-rb on 18.02.19.
 */
interface AuthInteractor {

    suspend fun authenticate(credentials: UserCredentials): Result<Unit>

    suspend fun isAuthenticated(): Boolean
}