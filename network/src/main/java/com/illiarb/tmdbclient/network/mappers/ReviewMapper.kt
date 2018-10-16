package com.illiarb.tmdbclient.network.mappers

import com.illiarb.tmdbclient.network.responses.ReviewResponse
import com.illiarb.tmdblcient.core.entity.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor() : Mapper<ReviewResponse, Review> {

    override fun map(from: ReviewResponse): Review =
        Review(
            from.author,
            from.content,
            from.url
        )
}