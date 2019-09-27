package com.tmdbclient.service_tmdb.configuration

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * @author ilya-rb on 30.11.18.
 */
data class ImageConfig(@SerializedName("base_url") var baseUrl: String) : Persistable {

    override fun readExternal(input: DataInput) {
        baseUrl = input.readString()
    }

    override fun writeExternal(output: DataOutput) = output.writeString(baseUrl)

    override fun deepClone(): Persistable = this
}