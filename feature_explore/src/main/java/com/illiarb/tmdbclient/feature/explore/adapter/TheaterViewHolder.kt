package com.illiarb.tmdbclient.feature.explore.adapter

import android.view.View
import com.illiarb.tmdbclient.feature.explore.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdblcient.core.entity.Location
import kotlinx.android.synthetic.main.item_nearby_theater.view.*

/**
 * @author ilya-rb on 06.12.18.
 */
class TheaterViewHolder(containerView: View) : BaseViewHolder<Location>(containerView) {

    private val itemTitle = itemView.itemNearbyTheaterName
    private val itemDistance = itemView.itemNearbyTheaterDistance

    override fun bind(item: Location) {
        itemTitle.text = item.title
        itemDistance.text = itemView.resources.getString(R.string.theater_distance, item.distance)
    }

    override fun onViewRecycled() {
    }
}