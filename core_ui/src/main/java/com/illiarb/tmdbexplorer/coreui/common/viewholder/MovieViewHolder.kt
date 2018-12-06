package com.illiarb.tmdbexplorer.coreui.common.viewholder

import android.view.View
import com.illiarb.tmdbexplorer.coreui.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * @author ilya-rb on 06.12.18.
 */
class MovieViewHolder(containerView: View) : BaseViewHolder<Movie>(containerView) {

    private val itemImage = itemView.itemMoviePoster
    private val imageCornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.image_corner_radius)

    override fun bind(item: Movie) {
        ImageLoader.loadImage(itemImage, item.posterPath, true, imageCornerRadius)
    }

    override fun bindClickListener(clickListener: View.OnClickListener) {
        itemView.setOnClickListener(clickListener)
    }

    override fun onViewRecycled() {
        ImageLoader.clearImageView(itemImage)
    }
}