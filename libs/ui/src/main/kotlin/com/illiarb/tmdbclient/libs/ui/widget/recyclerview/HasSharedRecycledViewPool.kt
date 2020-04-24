package com.illiarb.tmdbclient.libs.ui.widget.recyclerview

import androidx.recyclerview.widget.RecyclerView

interface HasSharedRecycledViewPool {

  fun setSharedViewPool(viewPool: RecyclerView.RecycledViewPool)
}