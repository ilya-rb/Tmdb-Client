package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.domain.auth.Authenticate
import com.illiarb.tmdblcient.core.domain.auth.ValidateCredentials
import com.illiarb.tmdblcient.core.domain.movie.GetAllMovies
import com.illiarb.tmdblcient.core.domain.movie.GetMovieDetails
import com.illiarb.tmdblcient.core.domain.movie.GetMovieFilters
import com.illiarb.tmdblcient.core.domain.movie.GetMovieReviews
import com.illiarb.tmdblcient.core.domain.movie.SearchMovies
import com.illiarb.tmdblcient.core.domain.profile.GetProfile

/**
 * @author ilya-rb on 08.01.19.
 */
interface UseCaseProvider {

    fun getMovieReviews(): GetMovieReviews

    fun getMovieDetails(): GetMovieDetails

    fun getMovieFilters(): GetMovieFilters

    fun getAllMovies(): GetAllMovies

    fun searchMovies(): SearchMovies

    fun authenticate(): Authenticate

    fun validateCredentials(): ValidateCredentials

    fun getProfile(): GetProfile
}