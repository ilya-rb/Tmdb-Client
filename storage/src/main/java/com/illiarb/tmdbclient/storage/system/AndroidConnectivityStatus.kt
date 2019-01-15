package com.illiarb.tmdbclient.storage.system

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.illiarb.tmdblcient.core.system.ConnectivityStatus
import com.illiarb.tmdblcient.core.system.ConnectivityStatus.ConnectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class AndroidConnectivityStatus @Inject constructor(private val context: Context) : ConnectivityStatus {

    companion object {
        private const val INTENT_FILTER_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val statusChannel: Channel<ConnectionState> = Channel(Channel.RENDEZVOUS)

    @SuppressLint("MissingPermission")
    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isConnected) {
//                statusChannel.send(ConnectionState.CONNECTED)
            } else {
//                statusChannel.send(ConnectionState.NOT_CONNECTED)
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun connectionState(): ReceiveChannel<ConnectionState> {
        registerReceiver()

        statusChannel.invokeOnClose {
            unregisterReceiver()
        }

        return statusChannel.also { channel ->
            // channel.send(getConnectionStatus())
        }
    }

    private fun getConnectionStatus() =
        if (connectivityManager.isDefaultNetworkActive) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.NOT_CONNECTED
        }

    private fun registerReceiver() {
        context.registerReceiver(connectionReceiver, IntentFilter(INTENT_FILTER_CONNECTIVITY_CHANGE))
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(connectionReceiver)
    }
}