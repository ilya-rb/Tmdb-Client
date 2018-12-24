package com.illiarb.tmdbclient.feature.movies.details

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.illiarb.tmdblcient.core.entity.Movie

/**
 * @author ilya-rb on 24.12.18.
 */
interface MovieDetailsView : MvpView {

    @StateStrategyType(SingleStateStrategy::class)
    fun showMovieDetails(movie: Movie)

    @StateStrategyType(SkipStrategy::class)
    fun showError(message: String)
}