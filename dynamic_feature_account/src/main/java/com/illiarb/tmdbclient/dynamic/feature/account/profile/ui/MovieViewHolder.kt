package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.view.View
import android.widget.ImageView
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdblcient.core.entity.Movie

/**
 * @author ilya-rb on 28.12.18.
 */
class MovieViewHolder(
    containerView: View,
    private val imageLoader: ImageLoader
) : BaseViewHolder<Movie>(containerView) {

    private val itemImage = itemView.findViewById<ImageView>(R.id.itemMoviePoster)
    private val imageCornerRadius = 10

    override fun bind(item: Movie) {
        imageLoader.fromUrl(item.posterPath, itemImage, ImageLoader.RequestOptions.create {
            cornerRadius(imageCornerRadius)
            cropOptions(ImageLoader.CropOptions.CENTER_CROP)
        })
    }

    override fun bindClickListener(clickListener: View.OnClickListener) {
        itemView.setOnClickListener(clickListener)
    }

    override fun onViewRecycled() {
        imageLoader.clearTarget(itemImage)
    }
}