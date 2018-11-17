package com.illiarb.tmdbclient.storage.model

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.util.Collections

data class MovieModel(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("release_date")
    var releaseDate: String = "",

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("title")
    var title: String = "",

    @SerializedName("vote_average")
    var voteAverage: Float = 0f,

    @SerializedName("budget")
    val budget: Int = 0,

    @SerializedName("genres")
    val genres: List<GenreModel>? = Collections.emptyList(),

    @SerializedName("homepage")
    val homepage: String? = null,

    @SerializedName("runtime")
    val runtime: Int = 0,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("reviews")
    val reviews: ResultsModel<ReviewModel>? = null,

    @SerializedName("images")
    val images: BackdropListModel? = null,

    @SerializedName("credits")
    val credits: CreditsModel? = null

) : Persistable {

    override fun readExternal(input: DataInput) =
        with(input) {
            id = readInt()
            posterPath = readString()
            backdropPath = readString()
            releaseDate = readString()
            overview = readString()
            title = readString()
            voteAverage = input.readFloat()
        }

    override fun deepClone(): Persistable =
        MovieModel(
            id,
            posterPath,
            backdropPath,
            releaseDate,
            overview,
            title,
            voteAverage
        )

    override fun writeExternal(output: DataOutput) =
        output.run {
            writeInt(id)
            writeString(posterPath)
            writeString(backdropPath)
            writeString(releaseDate)
            writeString(overview)
            writeString(title)
            writeFloat(voteAverage)
        }
}