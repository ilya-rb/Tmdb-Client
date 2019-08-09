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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
@ExperimentalCoroutinesApi
@FlowPreview
class AndroidConnectivityStatus @Inject constructor(private val app: App) : ConnectivityStatus {

    private val context: Context
        get() = app.getApplication()

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectionStatusChannel = Channel<ConnectionState>()

    @SuppressLint("MissingPermission")
    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val info = connectivityManager.activeNetworkInfo

            if (info != null && info.isConnected) {
                connectionStatusChannel.send(ConnectionState.CONNECTED)
            } else {
                connectionStatusChannel.send(ConnectionState.NOT_CONNECTED)
            }
        }
    }

    override fun connectionState(): Flow<ConnectionState> = connectionStatusChannel.consumeAsFlow()
        .onStart {
            registerReceiver()
            emit(getConnectionStatus())
        }
        .onCompletion { unregisterReceiver() }

    private fun getConnectionStatus() = if (connectivityManager.isDefaultNetworkActive) {
        ConnectionState.CONNECTED
    } else {
        ConnectionState.NOT_CONNECTED
    }

    private fun registerReceiver() {
        context.registerReceiver(connectionReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(connectionReceiver)
    }
}