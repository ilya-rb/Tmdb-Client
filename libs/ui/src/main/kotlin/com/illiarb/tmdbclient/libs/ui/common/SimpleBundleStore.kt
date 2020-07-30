package com.illiarb.tmdbclient.libs.ui.common

import android.os.Bundle
import android.os.Parcelable

class SimpleBundleStore {

  companion object {
    const val DEFAULT_SAVE_STATE_KEY = "simple_bundle_store"
  }

  private var bundle = Bundle()

  fun onRestoreInstanceState(key: String = DEFAULT_SAVE_STATE_KEY, savedInstanceState: Bundle?) {
    if (bundle.isEmpty) {
      savedInstanceState?.getBundle(key)?.let {
        bundle = Bundle(it)
      }
    }
  }

  fun onSaveInstanceState(key: String = DEFAULT_SAVE_STATE_KEY, outState: Bundle) {
    outState.putParcelable(key, bundle)
  }

  fun putParcelable(key: String, parcelable: Parcelable?) {
    bundle.putParcelable(key, parcelable)
  }

  fun putInt(key: String, value: Int) {
    bundle.putInt(key, value)
  }

  fun getInt(key: String, fallback: Int = 0): Int {
    return bundle.getInt(key, fallback)
  }

  fun getParcelable(key: String?): Parcelable? {
    val value = bundle.getParcelable<Parcelable?>(key)
    bundle.remove(key)
    return value
  }
}
