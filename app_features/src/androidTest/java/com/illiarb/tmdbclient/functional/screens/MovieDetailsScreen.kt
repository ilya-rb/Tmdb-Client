package com.illiarb.tmdbclient.functional.screens

import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.illiarb.tmdbclient.movies.home.R

class MovieDetailsScreen {

    val poster = KImageView { withId(R.id.movieDetailsPoster) }
    val playFab = KButton { withId(R.id.movieDetailsPlay) }
    val title = KTextView { withId(R.id.movieDetailsTitle) }
    val tags = KTextView { withId(R.id.movieDetailsTags) }
    val movieYear = KTextView { withId(R.id.movieDetailsYear) }
    val movieCountry = KTextView { withId(R.id.movieDetailsCountry) }
    val movieLength = KTextView { withId(R.id.movieDetailsLength) }
    val moviePhotos = KRecyclerView({ withId(R.id.movieDetailsPhotos) }, {})
}