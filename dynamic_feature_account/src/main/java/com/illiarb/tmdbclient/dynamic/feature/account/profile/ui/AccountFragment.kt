package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbclient.dynamic.feature.account.profile.AccountModel
import com.illiarb.tmdbclient.dynamic.feature.account.profile.AccountUiState
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter.FavoritesAdapter
import com.illiarb.tmdbexplorer.coreui.StateObserver
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Account
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment<AccountModel>(), Injectable, StateObserver<AccountUiState> {

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

        accountFavoritesList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritesAdapter
            setHasFixedSize(true)
            // TODO Fix this
            // addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_small), 0, false, false))
        }

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

    override fun onStateChanged(state: AccountUiState) {
        accountSwipeRefreshLayout.isRefreshing = state.isLoading

        state.account?.let {
            showAccount(it)
        }
    }

    private fun showAccount(account: Account) {
        accountAvatar.text = account.username.first().toUpperCase().toString()
        accountUsername.text = getString(R.string.account_username, account.username)
        accountAverageScore.text = account.averageRating.toString()
        accountAverageScoreProgress.progress = account.averageRating
        accountName.text = if (account.name.isEmpty()) {
            getString(R.string.account_name_fallback)
        } else {
            account.name
        }

        favoritesAdapter.submitList(account.favoriteMovies)
    }
}