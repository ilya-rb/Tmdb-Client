package com.illiarb.tmdbclient.services.tmdb.internal.network.serializer

import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.PersonModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.TrendingModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.TvShowModel
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrendingItemDeserializerTest {

  companion object {

    @JvmStatic
    @Suppress("unused")
    fun mediaTypeAndModelTypeProvider(): Stream<Arguments> {
      @Language("JSON") val movieJson = "{ \"media_type\": \"movie\" }"
      @Language("JSON") val tvJson = "{ \"media_type\": \"tv\" }"
      @Language("JSON") val personJson = "{ \"media_type\": \"person\" }"

      return Stream.of(
        arguments(movieJson, MovieModel::class),
        arguments(tvJson, TvShowModel::class),
        arguments(personJson, PersonModel::class)
      )
    }
  }

  private val gson = GsonBuilder()
    .registerTypeAdapter(TrendingModel::class.java, TrendingItemDeserializer())
    .create()

  @ParameterizedTest
  @MethodSource("mediaTypeAndModelTypeProvider")
  fun `it should convert item json to a correct type`(itemJson: String, expectedType: KClass<*>) {
    val result = gson.fromJson(itemJson, TrendingModel::class.java)
    assertThat(result::class).isEqualTo(expectedType)
  }
}