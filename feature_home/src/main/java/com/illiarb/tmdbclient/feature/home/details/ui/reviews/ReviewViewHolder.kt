package com.illiarb.tmdbclient.feature.home.details.ui.reviews

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Patterns
import android.view.View
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdblcient.core.domain.entity.Review
import kotlinx.android.synthetic.main.item_review.view.*

/**
 * @author ilya-rb on 18.10.18.
 */
class ReviewViewHolder(containerView: View) : BaseViewHolder<Review>(containerView) {

    private val reviewAuthor = itemView.itemReviewAuthor
    private val reviewText = itemView.itemReviewText
    private val reviewLink = itemView.itemReviewLink

    override fun bind(item: Review) {
        reviewAuthor.text = item.author

        reviewLink.text = item.url
        reviewLink.movementMethod = LinkMovementMethod.getInstance()

        Linkify.addLinks(reviewLink, Patterns.WEB_URL, item.url)

        reviewText.text = item.content
    }

    override fun onViewRecycled() {
    }
}