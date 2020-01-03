package com.tmdbclient.service_tmdb.model

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.util.Collections

data class GenreModel(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String
) : Persistable {

    constructor() : this(0, "")

    override fun readExternal(input: DataInput) {
        id = input.readInt()
        name = input.readString()
    }

    override fun deepClone(): Persistable = this

    override fun writeExternal(output: DataOutput) {
        output.writeInt(id)
        output.writeString(name)
    }
}

data class GenreListModel(@SerializedName("genres") var genres: List<GenreModel>) : Persistable {

    constructor() : this(Collections.emptyList())

    override fun readExternal(input: DataInput) {
        genres = mutableListOf<GenreModel>().apply {
            val size = input.readInt()
            for (i in 0 until size) {
                GenreModel()
                    .also { it.readExternal(input) }
                    .also { add(it) }
            }
        }
    }

    override fun deepClone(): Persistable = this

    override fun writeExternal(output: DataOutput) {
        output.writeInt(genres.size)

        genres.forEach {
            it.writeExternal(output)
        }
    }
}