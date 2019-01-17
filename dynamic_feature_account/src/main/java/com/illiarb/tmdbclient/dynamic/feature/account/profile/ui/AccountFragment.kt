package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountModel
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountUiState
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter.FavoritesAdapter
import com.illiarb.tmdbexplorer.coreui.observable.Observer
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutOrientation
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Account
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment<AccountModel>(), Injectable,
    Observer<AccountUiState> {

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    override fun getContentView(): Int = R.layout.fragment_account

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun getModelClass(): Class<AccountModel> = AccountModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountSwipeRefreshLayout.apply {
            isEnabled = false
            awareOfWindowInsets()
        }

        btnAccountLogout.setOnClickListener {
            presentationModel.onLogoutClick()
        }

        favoritesAdapter.clickEvent = { _, _, movie ->
            presentationModel.onFavoriteMovieClick(movie)
        }

        RecyclerViewBuilder
            .create {
                adapter(favoritesAdapter)
                type(LayoutType.Linear())
                orientation(LayoutOrientation.HORIZONTAL)
                hasFixedSize(true)
                spaceBetween {
                    horizontally = 16
                    addToFirst = true
                    addToLast = true
                }
            }
            .setupWith(accountFavoritesList)

        ViewCompat.requestApplyInsets(view)
    }

    override fun onStart() {
        super.onStart()
        presentationModel.observeState(this)
    }

    override fun onStop() {
        super.onStop()
        presentationModel.stopObserving(this)
    }

    override fun onNewValue(state: AccountUiState) {
        accountSwipeRefreshLayout.isRefreshing = state.isLoading

        state.account?.let {
            showAccount(it)
        }
    }

    private fun showAccount(account: Account) {
        accountAvatar.text = account.username.first().toUpperCase().toString()
        accountUsername.text = getString(R.string.account_username, account.username)
        accountAverageScore.text = account.averageRating.toString()
        accountName.text = if (account.name.isEmpty()) {
            getString(R.string.account_name_fallback)
        } else {
            account.name
        }

        favoritesAdapter.submitList(account.favoriteMovies)
    }
}