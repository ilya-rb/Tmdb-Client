package com.illiarb.tmdbclient.dynamic.feature.account.profile.domain

import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.AccountInteractor
import com.illiarb.tmdblcient.core.domain.entity.Account
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.storage.AccountRepository
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author ilya-rb on 18.02.19.
 */
class AccountInteractorImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val errorHandler: ErrorHandler,
    private val analyticsService: AnalyticsService
) : AccountInteractor {

    override suspend fun getAccount(): Result<Account> =
        Result.create(errorHandler) {
            val account = accountRepository.getCurrentAccount()

            val favorites = accountRepository.getFavoriteMovies(account.id)
            val rated = accountRepository.getRatedMovies(account.id)

            account.copy(
                averageRating = calculateRating(rated),
                favoriteMovies = favorites
            )
        }

    override suspend fun exitFromAccount(): Result<Unit> =
        Result
            .create(errorHandler) {
                accountRepository.clearAccountData()
                Unit
            }
            .doOnSuccess {
                analyticsService.trackEvent(analyticsService.factory.createLoggedOutEvent())
            }

    private fun calculateRating(movies: List<Movie>): Int =
        movies
            .map { it.rating }
            .average()
            .roundToInt() * 10
}