package com.illiarb.tmdbclient.feature.movies.details.info

import android.os.Bundle
import android.view.View
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.fragment_movie_details_info.*

class MovieDetailsInfoFragment : BaseFragment<BaseViewModel>() {

    companion object {

        private const val EXTRA_MOVIE = "movie"

        fun newInstance(movie: Movie): MovieDetailsInfoFragment {
            return MovieDetailsInfoFragment().apply {
                arguments = Bundle()
                    .apply {
                        putSerializable(EXTRA_MOVIE, movie)
                    }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val movie = args.getSerializable(EXTRA_MOVIE) as Movie

            movie.runtime?.let { runtime ->
                movieDetailsMeta.text = runtime.toString()
            }

            movie.overview?.let { overview ->
                movieDetailsOverview.text = overview
            }
        }
    }

    override fun getContentView(): Int = R.layout.fragment_movie_details_info

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java
}