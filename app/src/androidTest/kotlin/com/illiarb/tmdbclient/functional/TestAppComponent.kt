package com.illiarb.tmdbclient.functional

import android.app.Application
import androidx.fragment.app.FragmentFactory
import com.illiarb.tmdbclient.App
import com.illiarb.tmdbclient.AppBuildConfig
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.test.TestDependencies
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.navigation.DeepLinkHandler
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor
import com.illiarb.tmdbclient.system.DayNightModeChangeNotifier

class TestAppComponent(private val app: Application) : AppComponent {

  private val navigatorHolder = NavigatorHolder.ActionsBuffer()
  private val router = Router.DefaultRouter(navigatorHolder)

  override fun connectivityStatus(): ConnectivityStatus = TestDependencies.connectivityStatus()
  override fun moviesInteractor(): MoviesInteractor = TestDependencies.moviesInteractor()
  override fun analyticsService(): AnalyticsService = TestDependencies.analyticsService()
  override fun filtersInteractor(): FiltersInteractor = TestDependencies.filtersInteractor()
  override fun searchInteractor(): SearchInteractor = TestDependencies.searchInteractor()
  override fun discoverInteractor(): DiscoverInteractor = TestDependencies.discoverInteractor()
  override fun genresInteractor(): GenresInteractor = TestDependencies.genresInteractor()
  override fun navigatorHolder(): NavigatorHolder = navigatorHolder
  override fun router(): Router = router
  override fun inject(app: App) = Unit
  override fun buildConfig(): BuildConfig = AppBuildConfig(app)
  override fun fragmentFactory(): FragmentFactory = object : FragmentFactory() {}
  override fun deepLinkHandler(): DeepLinkHandler = DeepLinkHandler(router)
  override fun systemChangesNotifier(): DayNightModeChangeNotifier {
    return object : DayNightModeChangeNotifier {
      override fun notifySystemNightModeChanged(isEnabled: Boolean) {
      }
    }
  }
}