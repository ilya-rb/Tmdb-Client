package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbclient.dynamic.feature.account.profile.AccountModel
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter.FavoritesAdapter
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Account
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment(), Injectable, CoroutineScope {

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    @Inject
    lateinit var profileModel: AccountModel

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    override fun getContentView(): Int = R.layout.fragment_account

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
    }

    private fun setupUi(view: View) {
        accountSwipeRefreshLayout.isEnabled = false
        accountSwipeRefreshLayout.awareOfWindowInsets()

        btnAccountLogout.setOnClickListener {
            // TODO
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

    private fun render(state: AccountModel.ProfileState) {
        accountSwipeRefreshLayout.isRefreshing = state.isLoading

        state.account?.let {
            showAccount(it)
        }
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