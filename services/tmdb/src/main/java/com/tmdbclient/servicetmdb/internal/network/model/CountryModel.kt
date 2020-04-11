package com.tmdbclient.servicetmdb.internal.network.model

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import com.tmdbclient.servicetmdb.internal.cache.readPersistableList
import com.tmdbclient.servicetmdb.internal.cache.writePersistableList

internal data class CountryModel(

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

internal data class CountryList(var countries: List<CountryModel> = emptyList()) : Persistable {

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