package com.illiarb.tmdbclient.feature.movies.details.photos

import android.view.View
import android.widget.ImageView
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import kotlinx.android.synthetic.main.item_photo.view.*

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotoViewHolder(containerView: View) : BaseViewHolder<String>(containerView) {

    private val photo: ImageView = itemView.moviePhoto

    override fun bind(item: String) {
        ImageLoader.loadImage(photo, item, true)
    }

    override fun onViewRecycled() {
        ImageLoader.clearImageView(photo)
    }
}