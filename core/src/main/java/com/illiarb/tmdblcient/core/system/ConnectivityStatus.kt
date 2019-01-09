package com.illiarb.tmdblcient.core.system

import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author ilya-rb on 26.12.18.
 */
interface ConnectivityStatus {

    fun connectionState(): ReceiveChannel<ConnectionState>

    enum class ConnectionState {

        CONNECTED,

        NOT_CONNECTED
    }
}