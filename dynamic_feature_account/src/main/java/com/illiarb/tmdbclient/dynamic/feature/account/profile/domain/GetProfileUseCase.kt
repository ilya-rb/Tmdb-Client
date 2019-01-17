package com.illiarb.tmdbclient.dynamic.feature.account.profile.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.exception.ErrorHandler
import com.illiarb.tmdblcient.core.repository.AccountRepository
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author ilya-rb on 10.01.19.
 */
class GetProfileUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val errorHandler: ErrorHandler
) : NonBlockingUseCase<Account, Unit> {

    @NonBlocking
    override suspend fun executeAsync(payload: Unit): Result<Account> {
        return Result.create(errorHandler) {
            val currentAccount = accountRepository.getCurrentAccount()

            val favorite = accountRepository.getFavoriteMovies(currentAccount.id)
            val rated = accountRepository.getRatedMovies(currentAccount.id)

            val rating = calculateAverageRating(rated)

            currentAccount.copy(averageRating = rating, favoriteMovies = favorite)
        }
    }

    private fun calculateAverageRating(movies: List<Movie>): Int =
        movies
            .map { it.rating }
            .average()
            .roundToInt() * 10
}