package com.illiarb.tmdbclient.feature.home.details.presentation

import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdblcient.core.domain.entity.Review

/**
 * @author ilya-rb on 03.02.19.
 */
data class ShowReviewsAction(val reviews: List<Review>) : UiAction