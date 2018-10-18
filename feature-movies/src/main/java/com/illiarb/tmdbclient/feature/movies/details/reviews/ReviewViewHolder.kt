package com.illiarb.tmdbclient.feature.movies.details.reviews

import android.view.View
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdblcient.core.entity.Review
import kotlinx.android.synthetic.main.item_review.view.*

/**
 * @author ilya-rb on 18.10.18.
 */
class ReviewViewHolder(containerView: View) : BaseViewHolder<Review>(containerView) {

    private val reviewAuthor = itemView.itemReviewAuthor
    private val reviewText = itemView.itemReviewText

    override fun bind(item: Review) {
        reviewAuthor.text = item.author
        reviewText.text = item.content
    }

    override fun onViewRecycled() {
    }
}