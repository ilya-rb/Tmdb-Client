package com.illiarb.tmdbclient.tools

import kotlinx.coroutines.flow.Flow

/**
 * @author ilya-rb on 26.12.18.
 */
interface ConnectivityStatus {

  fun connectionState(): Flow<ConnectionState>

  enum class ConnectionState {

    CONNECTED,

    NOT_CONNECTED
  }
}