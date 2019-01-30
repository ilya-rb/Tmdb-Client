package com.illiarb.tmdbclient.storage.system

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.illiarb.tmdblcient.core.system.ConnectivityStatus
import com.illiarb.tmdblcient.core.system.ConnectivityStatus.ConnectionState
import com.illiarb.tmdblcient.core.util.observable.Observable
import com.illiarb.tmdblcient.core.util.observable.Observer
import com.illiarb.tmdblcient.core.util.observable.SimpleObservable
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class AndroidConnectivityStatus @Inject constructor(
    private val context: Context
) : ConnectivityStatus {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val statusObservable = object : SimpleObservable<ConnectionState>() {

        override fun addObserver(observer: Observer<ConnectionState>) {
            super.addObserver(observer)
            registerReceiver()
        }

        override fun removeObserver(observer: Observer<ConnectionState>) {
            super.removeObserver(observer)
            unregisterReceiver()
        }
    }
        .also { it.accept(getConnectionStatus()) }

    @SuppressLint("MissingPermission")
    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val info = connectivityManager.activeNetworkInfo

            if (info != null && info.isConnected) {
                statusObservable.accept(ConnectionState.CONNECTED)
            } else {
                statusObservable.accept(ConnectionState.NOT_CONNECTED)
            }
        }
    }

    override fun connectionState(): Observable<ConnectionState> = statusObservable

    private fun getConnectionStatus() =
        if (connectivityManager.isDefaultNetworkActive) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.NOT_CONNECTED
        }

    private fun registerReceiver() {
        context.registerReceiver(
            connectionReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(connectionReceiver)
    }
}