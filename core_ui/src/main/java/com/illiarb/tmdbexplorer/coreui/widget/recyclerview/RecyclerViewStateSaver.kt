package com.illiarb.tmdbexplorer.coreui.widget.recyclerview

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView

typealias StateSaver = () -> Parcelable?

class SimpleStateSaver(private val recyclerView: RecyclerView) : StateSaver {
    override fun invoke(): Parcelable? = recyclerView.layoutManager?.onSaveInstanceState()
}

class RecyclerViewStateSaver : LifecycleObserver {

    private val stateRegistry = mutableMapOf<String, StateSaver>()

    private var bundle = Bundle()

    fun saveInstanceState() {
        stateRegistry.forEach {
            bundle.putParcelable(it.key, it.value())
        }
    }

    fun registerAndRestoreIfNeeded(callback: StateSaver, key: String, recyclerView: RecyclerView) {
        stateRegistry[key] = callback

        val savedState = bundle.getParcelable<Parcelable?>(key)
        savedState?.let {
            recyclerView.layoutManager?.onRestoreInstanceState(savedState)
            bundle.remove(key)
        }
    }

    fun unregisterCallback(callback: StateSaver) {
        val entry = stateRegistry.entries.firstOrNull { it.value == callback }
        entry?.let {
            stateRegistry.remove(it.key)
        }
    }
}