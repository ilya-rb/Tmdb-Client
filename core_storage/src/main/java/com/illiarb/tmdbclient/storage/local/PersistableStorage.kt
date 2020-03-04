package com.illiarb.tmdbclient.storage.local

import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * @author ilya-rb on 26.10.18.
 */
fun <T : Persistable> BinaryPreferencesBuilder.registerPersistables(
  persistables: Map<String, Class<out T>>
): BinaryPreferencesBuilder = apply {
  persistables.forEach {
    registerPersistable(it.key, it.value)
  }
}

fun <T : Persistable> Preferences.getValue(key: String, result: T): T = getPersistable(key, result)

fun Preferences.putValue(key: String, value: Persistable) =
  edit().putPersistable(key, value).commit()

fun <T : Persistable> DataOutput.writePersistableList(list: Collection<T>) {
  if (list.isNotEmpty()) {
    writeInt(list.size)

    list.forEach {
      it.writeExternal(this)
    }
  }
}

fun <T : Persistable> DataInput.readPersistableList(
  outList: MutableCollection<T>,
  creator: () -> T
) {
  val size = readInt()
  for (i in 0 until size) {
    outList.add(creator().also { it.readExternal(this) })
  }
}

fun DataOutput.writeStringList(list: Collection<String>) {
  if (list.isNotEmpty()) {
    writeInt(list.size)

    list.forEach {
      writeString(it)
    }
  }
}

fun DataInput.readStringList(outList: MutableCollection<String>) {
  val size = readInt()

  for (i in 0 until size) {
    outList.add(readString())
  }
}