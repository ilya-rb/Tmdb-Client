package com.illiarb.tmdbclient.feature.movies.details.reviews

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_movie_details_reviews.*
import javax.inject.Inject

/**
 * @author ilya-rb on 18.10.18.
 */

class MovieDetailsReviewsFragment : BaseFragment<MovieDetailsReviewViewModel>(), Injectable {

    companion object {

        private const val ARG_MOVIE_ID = "id"

        fun newInstance(id: Int): MovieDetailsReviewsFragment {
            return MovieDetailsReviewsFragment().apply {
                arguments = Bundle()
                    .apply {
                        putInt(ARG_MOVIE_ID, id)
                    }
            }
        }
    }

    @Inject
    lateinit var reviewsAdapter: ReviewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewsList.let {
            it.adapter = reviewsAdapter
            it.layoutManager = LinearLayoutManager(requireActivity())
            it.isNestedScrollingEnabled = false

            val spacing = resources.getDimensionPixelSize(R.dimen.item_review_spacing)
            it.addItemDecoration(SpaceItemDecoration(0, spacing, true, true))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeMovieReviews()
            .subscribe(::onReviewsStateChanged, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)
    }

    override fun getContentView(): Int = R.layout.fragment_movie_details_reviews

    override fun getViewModelClass(): Class<MovieDetailsReviewViewModel> = MovieDetailsReviewViewModel::class.java

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider, requireActivity()).inject(this)

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            arguments?.let {
                viewModel.fetchReviews(it.getInt(ARG_MOVIE_ID))
            }
        }
    }

    private fun onReviewsStateChanged(uiState: UiState<List<Review>>) {
        if (uiState.hasData()) {
            reviewsAdapter.submitList(uiState.requireData())
        }
    }
}