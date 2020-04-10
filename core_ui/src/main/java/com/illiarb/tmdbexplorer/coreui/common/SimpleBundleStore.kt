package com.illiarb.tmdbexplorer.coreui.common

import android.os.Bundle
import android.os.Parcelable

class SimpleBundleStore {

  private val bundle = Bundle()

  fun saveInstanceState(block: Bundle.() -> Unit) {
    block(bundle)
  }

  fun getParcelable(key: String?): Parcelable? {
    val value = bundle.getParcelable<Parcelable?>(key)
    bundle.remove(key)
    return value
  }
}