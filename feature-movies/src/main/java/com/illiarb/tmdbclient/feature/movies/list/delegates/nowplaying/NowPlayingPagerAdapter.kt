package com.illiarb.tmdbclient.feature.movies.list.delegates.nowplaying

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.BlurParams
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import kotlinx.android.synthetic.main.item_now_playing.view.*

class NowPlayingPagerAdapter(
    private val uiEventsPipeline: EventPipeline<UiPipelineData>
) : PagerAdapter() {

    private val movies = mutableListOf<Movie>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        container.inflate(R.layout.item_now_playing)
            .apply { bindMovie(movies[position], this) }
            .also { container.addView(it) }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        if (item is View) {
            ImageLoader.clearImageView(item.itemNowPlayingCover)
            ImageLoader.clearImageView(item.itemNowPlayingPosterSmall)
            container.removeView(item)
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = movies.size

    fun setMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    private fun bindMovie(movie: Movie, view: View) {
        val radius = view.resources.getDimensionPixelSize(R.dimen.image_corner_radius)

        movie.posterPath?.let {
            ImageLoader.loadImage(view.itemNowPlayingPosterSmall, it, cornerRadius = radius)
        }

        ImageLoader.loadImage(view.itemNowPlayingCover, movie.backdropPath, true, radius, BlurParams())

        view.itemNowPlayingTitle.text = movie.title
        view.itemNowPlayingRating.text = movie.voteAverage.toString()

        view.setOnClickListener {
            uiEventsPipeline.dispatchEvent(MoviePipelineData(movie))
        }
    }
}