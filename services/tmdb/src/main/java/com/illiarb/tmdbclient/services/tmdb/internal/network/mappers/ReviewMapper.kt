package com.illiarb.tmdbclient.services.tmdb.internal.network.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.domain.Review
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.ReviewModel
import javax.inject.Inject

internal class ReviewMapper @Inject constructor() : Mapper<ReviewModel, Review> {

  override fun map(from: ReviewModel): Review = Review(from.author, from.content, from.url)
}