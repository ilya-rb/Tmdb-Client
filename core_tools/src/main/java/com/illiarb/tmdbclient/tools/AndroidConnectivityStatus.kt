package com.illiarb.tmdbclient.tools

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus.ConnectionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class AndroidConnectivityStatus @Inject constructor(private val app: App) : ConnectivityStatus {

    private val connectivityManager =
        app.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun connectionState(): Flow<ConnectionState> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                offer(getConnectionStatus())
            }
        }

        app.getApplication().registerReceiver(
            receiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )

        offer(getConnectionStatus())

        awaitClose {
            app.getApplication().unregisterReceiver(receiver)
        }
    }
        .onStart { emit(getConnectionStatus()) }

    @SuppressLint("MissingPermission")
    private fun isConnectedToNetwork(): Boolean =
        connectivityManager.activeNetworkInfo?.isConnected ?: false

    private fun getConnectionStatus() =
        if (isConnectedToNetwork()) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.NOT_CONNECTED
        }
}