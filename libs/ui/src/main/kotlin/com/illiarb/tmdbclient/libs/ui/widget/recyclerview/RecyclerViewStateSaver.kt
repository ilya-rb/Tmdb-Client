package com.illiarb.tmdbclient.libs.ui.widget.recyclerview

import android.os.Bundle

typealias StateSaver = Bundle.() -> Unit

class RecyclerViewStateSaver {

  private val stateRegistry = mutableMapOf<String, StateSaver>()
  private val bundle = Bundle()

  fun saveInstanceState() {
    stateRegistry.forEach {
      it.value(bundle)
    }
  }

  fun registerStateSaver(key: String, callback: StateSaver) {
    stateRegistry[key] = callback
  }

  fun unregisterStateSaver(key: String) {
    stateRegistry.remove(key)
  }

  fun state(key: String?, defaultValue: Any? = null): Any? {
    val state = bundle.get(key) ?: defaultValue

    return state.also {
      bundle.remove(key)
    }
  }
}