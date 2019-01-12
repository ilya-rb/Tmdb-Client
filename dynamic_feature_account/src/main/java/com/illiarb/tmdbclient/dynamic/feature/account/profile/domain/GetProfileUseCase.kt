package com.illiarb.tmdbclient.dynamic.feature.account.profile.domain

import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.repository.AccountRepository
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author ilya-rb on 10.01.19.
 */
class GetProfileUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : NonBlockingUseCase<Account, Unit> {

    @NonBlocking
    override suspend fun executeAsync(payload: Unit): Account {
        val currentAccount = accountRepository.getCurrentAccount()

        val favorite = accountRepository.getFavoriteMovies(currentAccount.id)
        val rated = accountRepository.getRatedMovies(currentAccount.id)

        val rating = calculateAverageRating(rated)

        return currentAccount.copy(averageRating = rating, favoriteMovies = favorite)
    }

    private fun calculateAverageRating(movies: List<Movie>): Int =
        movies
            .map { it.rating }
            .average()
            .roundToInt() * 10
}