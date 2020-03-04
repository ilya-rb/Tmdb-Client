package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.util.Mapper
import com.tmdbclient.servicetmdb.model.ReviewModel
import javax.inject.Inject

class ReviewMapper @Inject constructor() : Mapper<ReviewModel, Review> {

  override fun map(from: ReviewModel): Review = Review(from.author, from.content, from.url)
}