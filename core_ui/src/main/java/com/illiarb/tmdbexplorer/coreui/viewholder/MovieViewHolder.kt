package com.illiarb.tmdbexplorer.coreui.viewholder

import android.view.View
import com.illiarb.tmdbexplorer.coreui.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader.RequestOptions
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * @author ilya-rb on 06.12.18.
 */
class MovieViewHolder(
    containerView: View,
    private val imageLoader: ImageLoader
) : BaseViewHolder<Movie>(containerView) {

    private val itemImage = itemView.itemMoviePoster
    private val imageCornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.image_corner_radius)

    override fun bind(item: Movie) {
        imageLoader.fromUrl(item.posterPath, itemImage, RequestOptions.create {
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