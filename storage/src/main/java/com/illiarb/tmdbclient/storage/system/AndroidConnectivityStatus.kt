package com.illiarb.tmdbclient.storage.system

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.illiarb.tmdblcient.core.system.ConnectivityStatus
import com.illiarb.tmdblcient.core.system.ConnectivityStatus.ConnectionState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
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

    private val connectionSubject = PublishSubject.create<ConnectionState>()

    @SuppressLint("MissingPermission")
    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isConnected) {
                connectionSubject.onNext(ConnectionState.CONNECTED)
            } else {
                connectionSubject.onNext(ConnectionState.NOT_CONNECTED)
            }
        }
    }

    override fun connectionState(): Observable<ConnectionState> =
        connectionSubject
            .doOnSubscribe { registerReceiver() }
            .doOnDispose { unregisterReceiver() }
            .startWith(getConnectionStatus())

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