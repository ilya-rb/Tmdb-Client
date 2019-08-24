package com.illiarb.tmdbclient.tools

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus.ConnectionState
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 26.12.18.
 */
@FlowPreview
class AndroidConnectivityStatus @Inject constructor(
    private val app: App
) : ConnectivityStatus, LifecycleObserver {

    private val connectivityManager =
        app.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectionStatusChannel = Channel<ConnectionState>()

    private var coroutineScope: CoroutineScope? = null

    @SuppressLint("MissingPermission")
    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            coroutineScope?.launch {
                val info = connectivityManager.activeNetworkInfo

                if (info != null && info.isConnected) {
                    connectionStatusChannel.send(ConnectionState.CONNECTED)
                } else {
                    connectionStatusChannel.send(ConnectionState.NOT_CONNECTED)
                }
            }
        }
    }

    override fun connectionState(scope: CoroutineScope): Flow<ConnectionState> =
        connectionStatusChannel.consumeAsFlow().onStart {
            coroutineScope = scope
            registerReceiver()
            emit(getConnectionStatus())
        }

    override fun release() {
        unregisterReceiver()
        coroutineScope = null
    }

    private fun getConnectionStatus() =
        if (connectivityManager.isDefaultNetworkActive) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.NOT_CONNECTED
        }

    private fun registerReceiver() {
        app.getApplication().registerReceiver(
            connectionReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    private fun unregisterReceiver() {
        app.getApplication().unregisterReceiver(connectionReceiver)
    }
}