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
    var id: Int = ID_NON_EXISTENT,

    @SerializedName("name")
    var name: String = "",

    @SerializedName("username")
    var username: String = "",

    @SerializedName("avatar")
    var avatar: Avatar = Avatar(Gravatar(""))

) : Persistable {

    companion object {
        const val ID_NON_EXISTENT = -1
    }

    override fun readExternal(input: DataInput) =
        input.run {
            id = input.readInt()
            name = input.readString()
            username = input.readString()
            avatar = Avatar(Gravatar(input.readString()))
        }

    override fun writeExternal(output: DataOutput) =
        output.run {
            writeInt(id)
            writeString(name)
            writeString(username)
            writeString(avatar.gravatar.hash)
        }

    override fun deepClone(): Persistable = AccountModel(id, name, username, avatar)

    fun isNonExistent(): Boolean = id == ID_NON_EXISTENT
}

data class Avatar(@SerializedName("gravatar") val gravatar: Gravatar)

data class Gravatar(@SerializedName("hash") val hash: String)