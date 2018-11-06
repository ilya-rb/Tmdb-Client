package com.illiarb.tmdbclient.feature.movies.list.delegates.movie

import android.view.View
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieViewHolder(containerView: View) : BaseViewHolder<Movie>(containerView) {

    private val itemImage = itemView.itemMoviePoster
    private val imageCornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.item_movie_corner_radius)

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