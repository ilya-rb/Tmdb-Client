package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdblcient.core.domain.auth.Authenticate
import com.illiarb.tmdblcient.core.domain.auth.AuthenticateImpl
import com.illiarb.tmdblcient.core.domain.auth.ValidateCredentials
import com.illiarb.tmdblcient.core.domain.auth.ValidateCredentialsImpl
import com.illiarb.tmdblcient.core.domain.movie.GetAllMovies
import com.illiarb.tmdblcient.core.domain.movie.GetAllMoviesImpl
import com.illiarb.tmdblcient.core.domain.movie.GetMovieDetails
import com.illiarb.tmdblcient.core.domain.movie.GetMovieDetailsImpl
import com.illiarb.tmdblcient.core.domain.movie.GetMovieFilters
import com.illiarb.tmdblcient.core.domain.movie.GetMovieFiltersImpl
import com.illiarb.tmdblcient.core.domain.movie.GetMovieReviews
import com.illiarb.tmdblcient.core.domain.movie.GetMovieReviewsImpl
import com.illiarb.tmdblcient.core.domain.movie.SearchMovies
import com.illiarb.tmdblcient.core.domain.movie.SearchMoviesImpl
import com.illiarb.tmdblcient.core.domain.profile.GetProfile
import com.illiarb.tmdblcient.core.domain.profile.GetProfileImpl
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
interface UseCaseModule {

    @Binds
    fun getMovieReviews(impl: GetMovieReviewsImpl): GetMovieReviews

    @Binds
    fun getMovieDetails(impl: GetMovieDetailsImpl): GetMovieDetails

    @Binds
    fun getMovieFilters(impl: GetMovieFiltersImpl): GetMovieFilters

    @Binds
    fun getAllMovies(impl: GetAllMoviesImpl): GetAllMovies

    @Binds
    fun searchMovies(impl: SearchMoviesImpl): SearchMovies

    @Binds
    fun authenticate(impl: AuthenticateImpl): Authenticate

    @Binds
    fun validateCredentials(impl: ValidateCredentialsImpl): ValidateCredentials

    @Binds
    fun getProfile(impl: GetProfileImpl): GetProfile
}