package com.illiarb.tmdbclient.feature.movies.details.ui.photos

import android.view.View
import android.widget.ImageView
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import kotlinx.android.synthetic.main.item_photo.view.*

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotoViewHolder(containerView: View) : BaseViewHolder<String>(containerView) {

    private val photo: ImageView = itemView.moviePhoto
    private val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.image_corner_radius)

    override fun bind(item: String) {
        ImageLoader.loadImage(photo, item, true, cornerRadius)
    }

    override fun onViewRecycled() {
        ImageLoader.clearImageView(photo)
    }
}