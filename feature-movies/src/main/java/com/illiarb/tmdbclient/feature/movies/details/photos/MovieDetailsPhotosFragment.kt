package com.illiarb.tmdbclient.feature.movies.details.photos

import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel

class MovieDetailsPhotosFragment : BaseFragment<BaseViewModel>() {

    override fun getContentView(): Int = R.layout.fragment_movie_details_photos

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java
}