package com.tmdbclient.servicetmdb.configuration

import com.google.gson.annotations.SerializedName
import com.illiarb.tmdbclient.storage.local.readStringList
import com.illiarb.tmdbclient.storage.local.writeStringList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.util.Collections

/**
 * @author ilya-rb on 30.11.18.
 */
data class Configuration(

    @SerializedName("images")
    var images: ImageConfig = ImageConfig(),

    @SerializedName("change_keys")
    var changeKeys: List<String> = Collections.emptyList()

) : Persistable {

    override fun readExternal(input: DataInput) {
        images.readExternal(input)
        changeKeys = mutableListOf<String>().also { input.readStringList(it) }
    }

    override fun deepClone(): Persistable = this

    override fun writeExternal(output: DataOutput) {
        images.writeExternal(output)
        output.writeStringList(changeKeys)
    }

    fun isNotEmpty(): Boolean = images.secureBaseUrl.isNotEmpty()
            && images.backdropSizes.isNotEmpty()
            && images.posterSizes.isNotEmpty()
            && images.profileSizes.isNotEmpty()
            && changeKeys.isNotEmpty()
}