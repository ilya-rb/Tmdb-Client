package com.illiarb.tmdbexplorer.functional.screens

import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.ui.details.MovieDetailsFragment

object MovieDetailsScreen : KScreen<MovieDetailsScreen>() {

    override val layoutId: Int?
        get() = R.layout.fragment_movie_details

    override val viewClass: Class<*>
        get() = MovieDetailsFragment::class.java

    val poster = KImageView { withId(R.id.movieDetailsPoster) }
    val playFab = KButton { withId(R.id.movieDetailsPlay) }
    val title = KTextView { withId(R.id.movieDetailsTitle) }
    val tags = KTextView { withId(R.id.movieDetailsTags) }
    val movieReleaseDate = KTextView { withId(R.id.movieDetailsDate) }
    val movieCountry = KTextView { withId(R.id.movieDetailsCountry) }
    val movieLength = KTextView { withId(R.id.movieDetailsLength) }
    val moviePhotos = KRecyclerView({ withId(R.id.movieDetailsPhotos) }, {})
}