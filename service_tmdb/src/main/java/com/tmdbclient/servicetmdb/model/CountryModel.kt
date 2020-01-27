package com.tmdbclient.servicetmdb.model

import com.google.gson.annotations.SerializedName
import com.illiarb.tmdbclient.storage.local.readPersistableList
import com.illiarb.tmdbclient.storage.local.writePersistableList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

data class CountryModel(

    @SerializedName("iso_3166_1")
    var code: String = "",

    @SerializedName("english_name")
    var name: String = ""

) : Persistable {

    override fun readExternal(input: DataInput) {
        code = input.readString()
        name = input.readString()
    }

    override fun deepClone(): Persistable = this

    override fun writeExternal(output: DataOutput) {
        output.writeString(code)
        output.writeString(name)
    }
}

data class CountryList(var countries: List<CountryModel> = emptyList()) : Persistable {

    override fun readExternal(input: DataInput) {
        countries = mutableListOf<CountryModel>().also {
            input.readPersistableList(it) { CountryModel() }
        }
    }

    override fun deepClone(): Persistable = this

    override fun writeExternal(output: DataOutput) {
        output.writePersistableList(countries)
    }
}