package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountModel
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountUiState
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.ShowSignOutDialog
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter.FavoritesAdapter
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.core_ui_recycler_view.LayoutOrientation
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdbexplorer.coreui.util.LawObserver
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.Account
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

/**
 * @author ilya-rb on 20.11.18.
 */
@ExperimentalCoroutinesApi
class AccountFragment : BaseFragment<AccountModel>() {

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    private val stateObserver: LawObserver<AccountUiState> by lazy(NONE) {
        LawObserver.create(presentationModel.stateObservable(), ::render)
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

        accountBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        accountSignOut.setOnClickListener {
            presentationModel.onLogoutClick()
        }

        favoritesAdapter.clickEvent = { _, _, movie ->
            presentationModel.onFavoriteMovieClick(movie)
        }

        com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
            .create {
                adapter(favoritesAdapter)
                type(com.illiarb.core_ui_recycler_view.LayoutType.Linear())
                orientation(com.illiarb.core_ui_recycler_view.LayoutOrientation.HORIZONTAL)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountFavoritesList.adapter = null
    }

    override fun handleAction(action: UiAction) {
        super.handleAction(action)

        if (action is ShowSignOutDialog) {
            showSignOutDialog()
        }
    }

    private fun render(state: AccountUiState) = runOnUi {
        accountSwipeRefreshLayout.isRefreshing = state.isLoading

        state.account?.let {
            showAccount(it)
        }
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.account_sign_out_title)
            .setMessage(R.string.account_sign_out_message)
            .setNegativeButton(R.string.account_sign_out_cancel, null)
            .setPositiveButton(R.string.account_sign_out_positive) { dialog, _ ->
                presentationModel.onSignOutConfirm()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showAccount(account: Account) {
        accountAvatar.text = account.username.first().toUpperCase().toString()
        accountUsername.text = getString(R.string.account_username, account.username)
        accountName.text = if (account.name.isEmpty()) {
            getString(R.string.account_name_fallback)
        } else {
            account.name
        }

        if (account.averageRating == 0) {
            accountAverageScore.hide()
            accountAverageScoreLabel.hide()
        } else {
            accountAverageScore.text = account.averageRating.toString()
        }

        favoritesAdapter.submitList(account.favoriteMovies)
    }
}