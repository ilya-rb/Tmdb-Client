package com.illiarb.tmdbclient.libs.tools.internal

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
internal class AndroidConnectivityStatus @Inject constructor(app: Application) :
  ConnectivityStatus {

  private val connectivityManager =
    app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  @SuppressLint("MissingPermission")
  override fun connectionState(): Flow<ConnectivityStatus.ConnectionState> = callbackFlow {
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        super.onAvailable(network)
        offer(ConnectivityStatus.ConnectionState.CONNECTED)
      }

      override fun onLost(network: Network) {
        super.onLost(network)
        offer(ConnectivityStatus.ConnectionState.NOT_CONNECTED)
      }
    }

    connectivityManager.registerNetworkCallback(
      NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build(),
      networkCallback
    )

    awaitClose {
      connectivityManager.unregisterNetworkCallback(networkCallback)
    }
  }
}