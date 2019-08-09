package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.view.View
import android.widget.ImageView
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdblcient.core.domain.entity.Movie

/**
 * @author ilya-rb on 28.12.18.
 */
class MovieViewHolder(
    containerView: View,
    private val imageLoader: com.illiarb.core_ui_image.ImageLoader
) : BaseViewHolder<Movie>(containerView) {

    private val itemImage = itemView.findViewById<ImageView>(R.id.itemMoviePoster)
    private val itemOverlay = itemView.findViewById<View>(R.id.itemMoviePosterOverlay)
    private val imageCornerRadius = 10

    override fun bind(item: Movie) {
        imageLoader.fromUrl(item.posterPath, itemImage, com.illiarb.core_ui_image.RequestOptions.create {
            cornerRadius(imageCornerRadius)
            cropOptions(com.illiarb.core_ui_image.CropOptions.CENTER_CROP)
        })
    }

    override fun bindClickListener(clickListener: View.OnClickListener) {
        itemOverlay.setOnClickListener(clickListener)
    }

    override fun onViewRecycled() {
        imageLoader.clearTarget(itemImage)
    }
}