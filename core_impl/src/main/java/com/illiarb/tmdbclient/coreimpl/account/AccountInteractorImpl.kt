package com.illiarb.tmdbclient.coreimpl.account

import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.modules.account.AccountInteractor
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author ilya-rb on 21.11.18.
 */
class AccountInteractorImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val router: Router,
    private val schedulerProvider: SchedulerProvider
) : AccountInteractor {

    override fun getCurrentAccount(): Single<Account> =
        accountRepository.getCurrentAccount()
            .flatMap { account ->
                Single
                    .zip(
                        accountRepository.getFavoriteMovies(account.id),
                        accountRepository.getRatedMovies(account.id),

                        BiFunction { favoriteMovies: List<Movie>, ratedMovies: List<Movie> ->
                            account.copy(
                                averageRating = createAverageRating(ratedMovies),
                                favoriteMovies = favoriteMovies
                            )
                        }
                    )
                    .subscribeOn(schedulerProvider.provideIoScheduler())
            }

    override fun onLogoutClicked() {
        accountRepository.clearAccountData()
            .doAfterTerminate { router.navigateTo(AuthScreen) }
            .subscribe()
    }

    override fun onFavoriteMovieClicked(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }

    private fun createAverageRating(ratedMovies: List<Movie>): Int =
        ratedMovies.map { it.rating }
            .average()
            .roundToInt() * 10
}