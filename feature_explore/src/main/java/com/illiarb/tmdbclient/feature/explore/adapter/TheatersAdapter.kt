package com.illiarb.tmdbclient.feature.explore.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.illiarb.tmdbclient.feature.explore.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdblcient.core.entity.Location
import javax.inject.Inject

/**
 * @author ilya-rb on 06.12.18.
 */
class TheatersAdapter @Inject constructor() : BaseAdapter<Location, TheaterViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheaterViewHolder =
        TheaterViewHolder(parent.inflate(R.layout.item_nearby_theater))
}