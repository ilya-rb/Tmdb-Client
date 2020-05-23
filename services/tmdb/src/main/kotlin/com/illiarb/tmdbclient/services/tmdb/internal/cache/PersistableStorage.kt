package com.illiarb.tmdbclient.services.tmdb.internal.cache

import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * @author ilya-rb on 26.10.18.
 */
internal fun <T : Persistable> BinaryPreferencesBuilder.registerPersistables(
  persistables: Map<String, Class<out T>>
): BinaryPreferencesBuilder = apply {
  persistables.forEach {
    registerPersistable(it.key, it.value)
  }
}

internal fun <T : Persistable> Preferences.getValue(key: String, result: T): T =
  getPersistable(key, result)

internal fun Preferences.putValue(key: String, value: Persistable) =
  edit().putPersistable(key, value).commit()

internal fun <T : Persistable> DataOutput.writePersistableList(list: Collection<T>) {
  if (list.isNotEmpty()) {
    writeInt(list.size)

    list.forEach {
      it.writeExternal(this)
    }
  }
}

internal fun <T : Persistable> DataInput.readPersistableList(
  outList: MutableCollection<T>,
  creator: () -> T
) {
  val size = readInt()
  for (i in 0 until size) {
    outList.add(creator().also { it.readExternal(this) })
  }
}

internal fun DataOutput.writeStringList(list: Collection<String>) {
  if (list.isNotEmpty()) {
    writeInt(list.size)

    list.forEach {
      writeString(it)
    }
  }
}

internal fun DataInput.readStringList(outList: MutableCollection<String>) {
  val size = readInt()

  for (i in 0 until size) {
    outList.add(readString())
  }
}