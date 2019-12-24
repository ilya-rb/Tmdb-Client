package com.illiarb.tmdbexplorer.coreui.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes viewResId: Int): View =
    LayoutInflater.from(context).inflate(viewResId, this, false)