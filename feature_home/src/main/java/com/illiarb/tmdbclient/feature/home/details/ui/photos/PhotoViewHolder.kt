package com.illiarb.tmdbclient.feature.home.details.ui.photos

import android.view.View
import android.widget.ImageView
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader.RequestOptions
import kotlinx.android.synthetic.main.item_photo.view.*

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotoViewHolder(
    containerView: View,
    private val imageLoader: ImageLoader
) : BaseViewHolder<String>(containerView) {

    private val photo: ImageView = itemView.moviePhoto
    private val radius = itemView.resources.getDimensionPixelSize(R.dimen.image_corner_radius)

    override fun bind(item: String) {
        imageLoader.fromUrl(item, photo, RequestOptions.create {
            cornerRadius(radius)
            cropOptions(ImageLoader.CropOptions.CENTER_CROP)
        })
    }

    override fun onViewRecycled() {
        imageLoader.clearTarget(photo)
    }
}