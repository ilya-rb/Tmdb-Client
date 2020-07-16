package com.illiarb.tmdbclient.services.tmdb.interactor

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.libs.test.interactor.TestGenresInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestMoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.domain.GenresSection
import com.illiarb.tmdbclient.services.tmdb.domain.ListSection
import com.illiarb.tmdbclient.services.tmdb.domain.NowPlayingSection
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultHomeInteractor
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class HomeInteractorTest {

  private val interactor = DefaultHomeInteractor(
    TestMoviesInteractor(),
    TestGenresInteractor()
  )

  @Test
  fun `sections should contain genres section`() = runBlockingTest {
    val sections = interactor.getHomeSections().unwrap().map { it::class }
    assertThat(sections).contains(GenresSection::class)
  }

  @Test
  fun `sections should contain now playing section on top of other movie sections`() = runBlockingTest {
    val sections = interactor.getHomeSections().unwrap().map { it::class }

    assertThat(sections).contains(NowPlayingSection::class)

    val nowPlayingSectionPosition = sections.indexOfFirst { it == NowPlayingSection::class }
    val sectionsList = sections.subList(0, nowPlayingSectionPosition)

    assertThat(sectionsList.map { it::class }).containsNoneIn(arrayOf(ListSection::class))
  }

  @Test
  fun `sections should contain genres only limited to max size`() = runBlockingTest {
    val sections = interactor.getHomeSections().unwrap()

    // Check that genres section is present
    assertThat(sections.map { it::class }).contains(GenresSection::class)

    val genresSection = sections.first { it is GenresSection } as GenresSection
    assertThat(genresSection.genres.size).isAtMost(HomeInteractor.GENRES_MAX_SIZE)
  }
}