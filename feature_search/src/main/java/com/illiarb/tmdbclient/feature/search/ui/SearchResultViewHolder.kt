package com.illiarb.tmdbclient.feature.search.ui

import android.view.View
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdblcient.core.domain.entity.Movie

/**
 * @author ilya-rb on 27.12.18.
 */
class SearchResultViewHolder(
    containerView: View,
    private val imageLoader: com.illiarb.core_ui_image.ImageLoader
) : BaseViewHolder<Movie>(containerView) {

    private val itemTitle = itemView.itemSearchTitle
    private val itemImage = itemView.itemSearchImage
    private val itemRating = itemView.itemSearchRating

    override fun bind(item: Movie) {
        itemTitle.text = item.title
        itemRating.text = item.voteAverage.toString()

        item.backdropPath?.let {
            imageLoader.fromUrl(it, itemImage, com.illiarb.core_ui_image.RequestOptions.create {
                cropOptions(com.illiarb.core_ui_image.CropOptions.CENTER_CROP)
            })
        }
    }

    override fun bindClickListener(clickListener: View.OnClickListener) {
        itemView.setOnClickListener(clickListener)
    }

    override fun onViewRecycled() {
        imageLoader.clearTarget(itemImage)
    }
}