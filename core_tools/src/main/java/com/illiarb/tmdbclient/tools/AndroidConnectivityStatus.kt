package com.illiarb.tmdbclient.tools

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus.ConnectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
@ExperimentalCoroutinesApi
class AndroidConnectivityStatus @Inject constructor(app: App) : ConnectivityStatus {

    private val connectivityManager =
        app.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("MissingPermission")
    override fun connectionState(): Flow<ConnectionState> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                offer(ConnectionState.CONNECTED)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                offer(ConnectionState.NOT_CONNECTED)
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