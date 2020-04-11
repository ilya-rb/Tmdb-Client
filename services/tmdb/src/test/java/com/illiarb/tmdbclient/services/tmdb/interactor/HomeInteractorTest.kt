package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.libs.test.interactor.TestGenresInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestMoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.domain.GenresSection
import com.illiarb.tmdbclient.services.tmdb.domain.ListSection
import com.illiarb.tmdbclient.services.tmdb.domain.NowPlayingSection
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultHomeInteractor
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeInteractorTest {

  private val interactor = DefaultHomeInteractor(
    TestMoviesInteractor(),
    TestGenresInteractor()
  )

  @Test
  fun `should contain genres section if not empty`() = runBlockingTest {
    val sections = interactor.getHomeSections().unwrap()
    assertTrue(sections.any { it is GenresSection })
  }

  @Test
  fun `should contain now playing section on top of other movie sections`() = runBlockingTest {
    val sections = interactor.getHomeSections().unwrap()

    // Check that now playing section is present
    assertTrue(sections.any { it is NowPlayingSection })

    val nowPlayingSectionPosition = sections.indexOfFirst { it is NowPlayingSection }
    val sectionsList = sections.subList(0, nowPlayingSectionPosition)

    // Check that there is no list section
    // on top of now playing section
    assertTrue(sectionsList.none { it is ListSection })
  }

  @Test
  fun `should contain genres only limited to max size`() = runBlockingTest {
    val sections = interactor.getHomeSections().unwrap()

    // Check that genres section is present
    assertTrue(sections.any { it is GenresSection })

    val genresSection = sections.first { it is GenresSection } as GenresSection
    assertTrue(genresSection.genres.size <= HomeInteractor.GENRES_MAX_SIZE)
  }
}