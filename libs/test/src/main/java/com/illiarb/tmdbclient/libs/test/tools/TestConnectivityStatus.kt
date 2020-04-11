package com.illiarb.tmdbclient.libs.test.tools

import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestConnectivityStatus : ConnectivityStatus {

  override fun connectionState(): Flow<ConnectivityStatus.ConnectionState> = flow {
    emit(ConnectivityStatus.ConnectionState.CONNECTED)
  }
}