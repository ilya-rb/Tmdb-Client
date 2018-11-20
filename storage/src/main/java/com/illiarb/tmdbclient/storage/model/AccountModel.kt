package com.illiarb.tmdbclient.storage.model

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * @author ilya-rb on 20.11.18.
 */
data class AccountModel(

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("username")
    var username: String,

    @SerializedName("avatar")
    var avatar: Gravatar

) : Persistable {

    override fun readExternal(input: DataInput) =
        input.run {
            id = input.readInt()
            name = input.readString()
            username = input.readString()
            avatar = Gravatar(input.readString())
        }

    override fun writeExternal(output: DataOutput) =
        output.run {
            writeInt(id)
            writeString(name)
            writeString(username)
            writeString(avatar.hash)
        }

    override fun deepClone(): Persistable = AccountModel(id, name, username, avatar)
}

data class Gravatar(@SerializedName("hash") val hash: String)