package com.illiarb.tmdblcient.core.tools

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * @author ilya-rb on 26.12.18.
 */
interface ConnectivityStatus {

    fun connectionState(scope: CoroutineScope): Flow<ConnectionState>

    fun release()

    enum class ConnectionState {

        CONNECTED,

        NOT_CONNECTED
    }
}