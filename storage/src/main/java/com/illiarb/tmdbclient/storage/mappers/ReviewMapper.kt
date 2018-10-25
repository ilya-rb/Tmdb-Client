package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.dto.ReviewDto
import com.illiarb.tmdblcient.core.entity.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor() : Mapper<ReviewDto, Review> {

    override fun map(from: ReviewDto): Review =
        Review(
            from.author,
            from.content,
            from.url
        )
}