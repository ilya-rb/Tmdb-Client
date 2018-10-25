package com.illiarb.tmdbclient.feature.movies.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.details.info.MovieDetailsInfoFragment
import com.illiarb.tmdbclient.feature.movies.details.photos.MovieDetailsPhotosFragment
import com.illiarb.tmdbclient.feature.movies.details.reviews.MovieDetailsReviewsFragment
import com.illiarb.tmdbclient.feature.movies.details.videos.MovieDetailsVideosFragment
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.system.ResourceResolver
import javax.inject.Inject

class MovieInfoPagerAdapter @Inject constructor(
    fragmentManager: FragmentManager,
    private val resourceResolver: ResourceResolver
) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        private const val TABS_COUNT = 3
    }

    var movie: Movie? = null

    override fun getItem(position: Int): Fragment? {
        return movie?.let {
            when (position) {
                0 -> MovieDetailsInfoFragment.newInstance(it.overview)
                1 -> MovieDetailsReviewsFragment.newInstance(it.id)
                2 -> MovieDetailsPhotosFragment()
                3 -> MovieDetailsVideosFragment()
                else -> throw IllegalArgumentException("Unknown tab position")
            }
        }
    }

    override fun getCount(): Int = TABS_COUNT

    override fun getPageTitle(position: Int): CharSequence? =
        when (position) {
            0 -> resourceResolver.getString(R.string.movie_details_info_tab_title)
            1 -> resourceResolver.getString(R.string.movie_details_info_reviews_tab_title)
            2 -> resourceResolver.getString(R.string.movie_details_info_photos_tab_title)
            3 -> resourceResolver.getString(R.string.movie_details_info_videos_tab_title)
            else -> throw IllegalArgumentException("Unknown tab position")
        }
}