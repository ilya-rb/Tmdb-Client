package com.illiarb.tmdbclient.feature.home.details.ui.reviews

import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdblcient.core.domain.entity.Review
import javax.inject.Inject

/**
 * @author ilya-rb on 18.10.18.
 */
class ReviewsAdapter @Inject constructor() : BaseAdapter<Review, ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(parent.inflate(R.layout.item_review))
}