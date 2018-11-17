package com.illiarb.tmdbclient.storage.model

import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.util.Collections

/**
 * @author ilya-rb on 16.11.18.
 */
data class MovieListModel(var movies: List<MovieModel>) : Persistable {

    constructor() : this(Collections.emptyList())

    override fun readExternal(input: DataInput) {
        movies = mutableListOf<MovieModel>().apply {
            val size = input.readInt()
            for (i in 0 until size) {
                MovieModel()
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

    override fun deepClone(): Persistable = this
}