package com.tmdbclient.servicetmdb.serializer

import com.google.gson.GsonBuilder
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.PersonModel
import com.tmdbclient.servicetmdb.model.TrendingModel
import com.tmdbclient.servicetmdb.model.TvShowModel
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.reflect.KClass

@RunWith(Parameterized::class)
class TrendingItemDeserializerTest(
  private val itemJson: String,
  private val expectedType: KClass<*>
) {

  companion object {

    @JvmStatic
    @Parameterized.Parameters(name = "{index}: itemJson={0}, expectedType={1}")
    fun data(): Iterable<Array<Any>> {
      @Language("JSON") val movieJson = "{ \"media_type\": \"movie\" }"
      @Language("JSON") val tvJson = "{ \"media_type\": \"tv\" }"
      @Language("JSON") val personJson = "{ \"media_type\": \"person\" }"

      return listOf(
        arrayOf(movieJson, MovieModel::class),
        arrayOf(tvJson, TvShowModel::class),
        arrayOf(personJson, PersonModel::class)
      )
    }
  }

  private val gson = GsonBuilder()
    .registerTypeAdapter(TrendingModel::class.java, TrendingItemDeserializer())
    .create()

  @Test
  fun `should convert json to correct type`() {
    val result = gson.fromJson(itemJson, TrendingModel::class.java)
    assertEquals(result::class, expectedType)
  }
}