package com.illiarb.tmdbclient.network.mappers

import com.illiarb.tmdbclient.network.responses.MovieResponse
import com.illiarb.tmdblcient.core.entity.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor(
    private val genreMapper: GenreMapper,
    private val personMapper: PersonMapper,
    private val companyMapper: CompanyMapper,
    private val reviewMapper: ReviewMapper,
    private val backdropMapper: BackdropMapper
) : Mapper<MovieResponse, Movie> {

    override fun map(from: MovieResponse): Movie =
        Movie(
            from.id,
            from.adult,
            from.backdropPath,
            from.budget,
            genreMapper.mapList(from.genres),
            from.homepage,
            from.imdbId,
            personMapper.mapList(from.credits?.cast),
            companyMapper.mapList(from.productionCompanies),
            from.releaseDate,
            from.overview,
            reviewMapper.mapList(from.reviews?.reviews),
            from.runtime,
            from.status,
            from.title,
            from.tagline,
            backdropMapper.mapList(from.images?.backdrops),
            from.voteAverage,
            from.voteCount
        )
}