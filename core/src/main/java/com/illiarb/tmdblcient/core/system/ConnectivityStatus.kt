package com.illiarb.tmdblcient.core.system

/**
 * @author ilya-rb on 26.12.18.
 */
interface ConnectivityStatus {

    // TODO
    fun connectionState(): ConnectionState

    enum class ConnectionState {

        CONNECTED,

        NOT_CONNECTED
    }
}