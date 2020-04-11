package com.tmdbclient.servicetmdb.internal.network.mappers

import com.illiarb.tmdbclient.util.Mapper
import com.tmdbclient.servicetmdb.domain.Review
import com.tmdbclient.servicetmdb.internal.network.model.ReviewModel
import javax.inject.Inject

internal class ReviewMapper @Inject constructor() : Mapper<ReviewModel, Review> {

  override fun map(from: ReviewModel): Review = Review(from.author, from.content, from.url)
}