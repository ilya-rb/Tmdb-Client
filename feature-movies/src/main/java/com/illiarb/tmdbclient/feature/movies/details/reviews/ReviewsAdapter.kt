package com.illiarb.tmdbclient.feature.movies.details.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdblcient.core.entity.Review
import javax.inject.Inject

/**
 * @author ilya-rb on 18.10.18.
 */
class ReviewsAdapter @Inject constructor() : BaseAdapter<Review, ReviewViewHolder>(diffCallback) {

    companion object {

        @JvmStatic
        private val diffCallback = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }
}