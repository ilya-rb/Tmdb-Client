package com.illiarb.tmdbclient.feature.home.list

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection

/**
 * @author ilya-rb on 24.12.18.
 */
interface MoviesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showMovieSections(movies: List<MovieSection>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showMovieFilters(filters: List<MovieFilter>)

    @StateStrategyType(SkipStrategy::class)
    fun showError(message: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()
}