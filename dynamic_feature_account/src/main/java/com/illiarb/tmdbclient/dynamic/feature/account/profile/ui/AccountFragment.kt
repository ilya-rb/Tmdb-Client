package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.badoo.mvicore.binder.Binder
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.Wish.*
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountState
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter.FavoritesAdapter
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment(), Injectable, Consumer<AccountState> {

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    @Inject
    lateinit var uiEventPipeline: EventPipeline<@JvmSuppressWildcards UiPipelineData>

    @Inject
    lateinit var feature: AccountFeature

    @Inject
    lateinit var router: Router

    private val binder = Binder()

    private val newsHandler = Consumer<AccountFeature.News> { news ->
        when (news) {
            is AccountFeature.News.ShowAuthScreen -> router.navigateTo(AuthScreen)
            is AccountFeature.News.ShowMovieDetails -> router.navigateTo(MovieDetailsScreen(news.id))
        }
    }

    override fun getContentView(): Int = R.layout.fragment_account

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        setupBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder.clear()
    }

    override fun accept(state: AccountState) {
        accountSwipeRefreshLayout.isRefreshing = state.isLoading

        if (state.isBlockingLoading) {
            showBlockingProgress()
        } else {
            hideBlockingProgress()
        }

        state.account?.let {
            showAccount(it)
        }
    }

    private fun setupUi(view: View) {
        accountSwipeRefreshLayout.isEnabled = false
        accountSwipeRefreshLayout.awareOfWindowInsets()

        btnAccountLogout.setOnClickListener {
            feature.accept(SignOut)
        }

        accountFavoritesList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritesAdapter
            setHasFixedSize(true)
            // TODO Fix this
//            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_small), 0, false, false))
        }

        uiEventPipeline.observeEvents()
            .ofType(MoviePipelineData::class.java)
            .subscribe({ feature.accept(ShowMovieDetails(it.movie.id)) }, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)

        ViewCompat.requestApplyInsets(view)
    }

    private fun setupBindings() {
        binder.bind(feature to this)
        binder.bind(feature.news to newsHandler)

        feature.accept(ShowAccount)
    }

    private fun showAccount(account: Account) {
        accountAvatar.text = account.username.first().toUpperCase().toString()
        accountUsername.text = getString(R.string.account_username, account.username)
        accountAverageScore.text = account.averageRating.toString()
        accountAverageScoreProgress.progress = 100//account.averageRating
        accountName.text = if (account.name.isEmpty()) {
            getString(R.string.account_name_fallback)
        } else {
            account.name
        }

        favoritesAdapter.submitList(account.favoriteMovies)
    }
}