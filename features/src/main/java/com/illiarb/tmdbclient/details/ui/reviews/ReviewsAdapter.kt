package com.illiarb.tmdbclient.details.ui.reviews

import android.view.ViewGroup
import com.illiarb.core_ui_recycler_view.SimpleAdapter
import com.illiarb.tmdbclient.home.R
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdblcient.core.domain.entity.Review
import javax.inject.Inject

/**
 * @author ilya-rb on 18.10.18.
 */
class ReviewsAdapter @Inject constructor() : SimpleAdapter<Review, ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(parent.inflate(R.layout.item_review))

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }
}