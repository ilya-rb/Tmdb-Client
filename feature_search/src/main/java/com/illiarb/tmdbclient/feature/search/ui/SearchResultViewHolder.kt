package com.illiarb.tmdbclient.feature.search.ui

import android.view.View
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.item_search_result.view.*

/**
 * @author ilya-rb on 27.12.18.
 */
class SearchResultViewHolder(containerView: View) : BaseViewHolder<Movie>(containerView) {

    private val itemTitle = itemView.itemSearchTitle
    private val itemImage = itemView.itemSearchImage
    private val itemRating = itemView.itemSearchRating

    override fun bind(item: Movie) {
        itemTitle.text = item.title
        itemRating.text = item.voteAverage.toString()

        item.backdropPath?.let {
            ImageLoader.loadImage(itemImage, it, centerCrop = true)
        }
    }

    override fun onViewRecycled() {
        ImageLoader.clearImageView(itemImage)
    }
}