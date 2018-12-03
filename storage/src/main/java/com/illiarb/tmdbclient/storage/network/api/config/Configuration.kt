package com.illiarb.tmdbclient.storage.network.api.config

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * @author ilya-rb on 30.11.18.
 */
data class Configuration(

    @SerializedName("images")
    var images: ImageConfig,

    @SerializedName("change_keys")
    var changeKeys: List<String>

) : Persistable {

    override fun readExternal(input: DataInput) {
        images.readExternal(input)
        changeKeys = mutableListOf<String>().apply {
            val size = input.readInt()
            for (key in 0..size) {
                add(input.readString())
            }
        }
    }

    override fun deepClone(): Persistable = this

    override fun writeExternal(output: DataOutput) {
        images.writeExternal(output)

        output.writeInt(changeKeys.size)

        changeKeys.forEach {
            output.writeString(it)
        }
    }
}