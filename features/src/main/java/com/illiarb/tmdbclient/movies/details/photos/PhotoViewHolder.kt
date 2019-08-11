package com.illiarb.tmdbclient.movies.details.photos

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import kotlinx.android.synthetic.main.item_photo.view.*

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotoViewHolder(
    containerView: View,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(containerView) {

    private val photo: ImageView = itemView.moviePhoto
    private val radius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)

    fun bind(item: String) {
        imageLoader.fromUrl(item, photo, RequestOptions.create {
            cornerRadius(radius)
            cropOptions(CropOptions.CENTER_CROP)
        })
    }
}