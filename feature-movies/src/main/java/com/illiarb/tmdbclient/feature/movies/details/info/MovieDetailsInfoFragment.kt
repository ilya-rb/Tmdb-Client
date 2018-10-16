package com.illiarb.tmdbclient.feature.movies.details.info

import android.os.Bundle
import android.view.View
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.fragment_movie_details_info.*

class MovieDetailsInfoFragment : BaseFragment<BaseViewModel>() {

    companion object {

        private const val EXTRA_OVERVIEW = "overview"

        fun newInstance(overview: String?): MovieDetailsInfoFragment {
            return MovieDetailsInfoFragment().apply {
                arguments = Bundle()
                    .apply {
                        putString(EXTRA_OVERVIEW, overview)
                    }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            movieDetailsOverview.text = args.getString(EXTRA_OVERVIEW, getString(R.string.movie_details_overview_empty))
        }
    }

    override fun getContentView(): Int = R.layout.fragment_movie_details_info

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java
}