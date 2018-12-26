package com.illiarb.tmdblcient.core.system

import io.reactivex.Observable

/**
 * @author ilya-rb on 26.12.18.
 */
interface ConnectivityStatus {

    fun connectionState(): Observable<ConnectionState>

    enum class ConnectionState {

        CONNECTED,

        NOT_CONNECTED
    }
}