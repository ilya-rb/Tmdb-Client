package com.tmdbclient.service_tmdb.model

import com.illiarb.tmdbclient.storage.local.readPersistableList
import com.illiarb.tmdbclient.storage.local.writePersistableList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.util.*

/**
 * @author ilya-rb on 16.11.18.
 */
data class MovieListModel(var movies: List<MovieModel>) : Persistable {

    constructor() : this(Collections.emptyList())

    override fun readExternal(input: DataInput) {
        movies = mutableListOf<MovieModel>().also {
            input.readPersistableList(it) { MovieModel() }
        }
    }

    override fun writeExternal(output: DataOutput) {
        output.writePersistableList(movies)
    }

    override fun deepClone(): Persistable = this
}