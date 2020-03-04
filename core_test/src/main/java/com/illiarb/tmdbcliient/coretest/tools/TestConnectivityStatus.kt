package com.illiarb.tmdbcliient.coretest.tools

import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestConnectivityStatus : ConnectivityStatus {

  override fun connectionState(): Flow<ConnectivityStatus.ConnectionState> = flow {
    emit(ConnectivityStatus.ConnectionState.CONNECTED)
  }
}