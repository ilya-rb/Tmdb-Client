package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.model.ReviewModel
import com.illiarb.tmdblcient.core.entity.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor() : Mapper<ReviewModel, Review> {
    override fun map(from: ReviewModel): Review = Review(from.author, from.content, from.url)
}