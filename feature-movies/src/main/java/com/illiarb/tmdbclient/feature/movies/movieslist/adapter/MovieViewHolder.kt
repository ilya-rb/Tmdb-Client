package com.illiarb.tmdbclient.feature.movies.movieslist.adapter

import android.view.View
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.setDateText
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieViewHolder(containerView: View) : BaseViewHolder<Movie>(containerView) {

    private val itemTitle = itemView.itemMovieTitle
    private val itemImage = itemView.itemMoviePoster
    private val itemReleaseDate = itemView.itemMovieReleaseDate
    private val itemRating = itemView.itemMovieRating

    private val imageCornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.item_movie_corner_radius)

    override fun bind(item: Movie) {
        itemTitle.text = item.title
        itemRating.text = item.voteAverage.toString()

        itemReleaseDate.setDateText(item.releaseDate)

        item.posterPath?.let {
            ImageLoader.loadImage(itemImage, it, true, imageCornerRadius)
        }
    }

    override fun bindClickListener(clickListener: View.OnClickListener) {
        itemView.setOnClickListener(clickListener::onClick)
    }

    override fun onViewRecycled() {
        ImageLoader.clearImageView(itemImage)
    }
}