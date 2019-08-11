package com.illiarb.tmdblcient.core.tools

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * @author ilya-rb on 26.12.18.
 */
interface ConnectivityStatus {

    fun connectionState(): Flow<ConnectionState>

    fun setCoroutineScope(scope: CoroutineScope)

    fun removeCoroutineScope()

    enum class ConnectionState {

        CONNECTED,

        NOT_CONNECTED
    }
}