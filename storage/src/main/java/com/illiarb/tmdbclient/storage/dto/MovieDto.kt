package com.illiarb.tmdbclient.storage.dto

import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

data class MovieDto(
    var id: Int,
    var adult: Boolean,
    var posterPath: String?,
    var budget: Int,
    var genres: MutableList<GenreDto>,
    var homepage: String?,
    var imdbId: String?,
    var credits: CreditsDto?,
    var productionCompanies: List<CompanyDto>?,
    var releaseDate: String,
    var overview: String?,
    var reviews: ReviewListDto?,
    var runtime: Int?,
    var status: String?,
    var title: String,
    var tagline: String?,
    var images: BackdropListDto?,
    var voteAverage: Float,
    var voteCount: Int
) : Persistable {

    constructor() : this(0, false, null, 0, mutableListOf<GenreDto>(), null, null, null, null, "", null, null, 0, null, "", null, null, 0f, 0)

    override fun readExternal(input: DataInput) =
        with(input) {
            id = readInt()
            posterPath = readString()
            releaseDate = readString()
            overview = readString()
            title = readString()
            voteAverage = input.readFloat()
        }

    override fun deepClone(): Persistable =
        MovieDto(
            id,
            false,
            posterPath,
            0,
            mutableListOf(),
            null,
            null,
            null,
            null,
            releaseDate,
            overview,
            null,
            0,
            null,
            title,
            null,
            null,
            voteAverage,
            0
        )

    override fun writeExternal(output: DataOutput) =
        output.run {
            writeInt(id)
            writeString(posterPath)
            writeString(releaseDate)
            writeString(overview)
            writeString(title)
            writeFloat(voteAverage)
        }
}

data class MovieListDto(var movies: List<MovieDto>) : Persistable {

    @Suppress("unused")
    constructor() : this(emptyList())

    override fun readExternal(input: DataInput) {
        movies = mutableListOf<MovieDto>().apply {
            val size = input.readInt()
            for (i in 0 until size) {
                MovieDto()
                    .also { it.readExternal(input) }
                    .also { add(it) }
            }
        }
    }

    override fun writeExternal(output: DataOutput) {
        output.writeInt(movies.size)

        movies.forEach {
            it.writeExternal(output)
        }
    }

    override fun deepClone(): Persistable {
        val result = mutableListOf<Persistable>().apply {
            movies.forEach {
                add(it.deepClone())
            }
        }
        @Suppress("UNCHECKED_CAST")
        return MovieListDto(result as List<MovieDto>)
    }
}