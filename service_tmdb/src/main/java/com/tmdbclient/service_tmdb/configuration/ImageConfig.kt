package com.tmdbclient.service_tmdb.configuration

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.util.Collections

/**
 * @author ilya-rb on 30.11.18.
 */
data class ImageConfig(
    @SerializedName("base_url") var baseUrl: String = "",
    @SerializedName("secure_base_url") var secureBaseUrl: String = "",
    @SerializedName("backdrop_sizes") var backdropSizes: List<String> = Collections.emptyList(),
    @SerializedName("profile_sizes") var posterSizes: List<String> = Collections.emptyList(),
    @SerializedName("poster_sizes") var profileSizes: List<String> = Collections.emptyList()
) : Persistable {

    override fun readExternal(input: DataInput) {
        baseUrl = input.readString()
        secureBaseUrl = input.readString()
        backdropSizes = mutableListOf<String>().also { input.readStringList(it) }
        posterSizes = mutableListOf<String>().also { input.readStringList(it) }
        profileSizes = mutableListOf<String>().also { input.readStringList(it) }
    }

    override fun writeExternal(output: DataOutput) {
        output.writeString(baseUrl)
        output.writeString(secureBaseUrl)
        output.writeStringList(backdropSizes)
        output.writeStringList(posterSizes)
        output.writeStringList(profileSizes)
    }

    override fun deepClone(): Persistable = this

    private fun DataOutput.writeStringList(list: List<String>) {
        writeInt(list.size)

        list.forEach {
            writeString(it)
        }
    }

    private fun DataInput.readStringList(outList: MutableList<String>) {
        val size = readInt()

        for (i in 0 until size) {
            outList.add(readString())
        }
    }
}