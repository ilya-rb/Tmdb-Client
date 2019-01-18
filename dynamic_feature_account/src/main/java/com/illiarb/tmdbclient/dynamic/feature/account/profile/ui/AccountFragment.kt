package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountModel
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountUiState
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter.FavoritesAdapter
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.observable.LifecycleAwareObserver
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutOrientation
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.uiactions.ShowErrorDialogAction
import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Account
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment<AccountModel>() {

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    private val stateObserver: LifecycleAwareObserver<AccountUiState> by lazy(NONE) {
        object : LifecycleAwareObserver<AccountUiState>(presentationModel.stateObservable()) {
            override fun onNewValue(state: AccountUiState) {
                render(state)
            }
        }
    }

    private val actionsObserver: LifecycleAwareObserver<UiAction> by lazy(NONE) {
        object : LifecycleAwareObserver<UiAction>(presentationModel.actionsObservable()) {
            override fun onNewValue(state: UiAction) {
                when (state) {
                    is ShowErrorDialogAction -> showErrorDialog(state.message)
                }
            }
        }
    }

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
                    spacing = resources.getDimensionPixelSize(R.dimen.account_favorites_horizontal_space)
                }
            }
            .setupWith(accountFavoritesList)

        ViewCompat.requestApplyInsets(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        stateObserver.register(this)
        actionsObserver.register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // accountFavoritesList.adapter = null
    }

    private fun render(state: AccountUiState) {
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