package com.illiarb.tmdbclient.dynamic.feature.account

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment<AccountViewModel>(), Injectable {

    override fun getContentView(): Int = R.layout.fragment_account

    override fun getViewModelClass(): Class<AccountViewModel> = AccountViewModel::class.java

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountSwipeRefreshLayout.isEnabled = false
        accountSwipeRefreshLayout.awareOfWindowInsets()

        btnAccountLogout.setOnClickListener {
            viewModel.onLogoutClicked()
        }

        ViewCompat.requestApplyInsets(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeAccountState()
            .subscribe(::onAccountStateChanged, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)
    }

    private fun onAccountStateChanged(uiState: UiState<Account>) {
        accountSwipeRefreshLayout.isRefreshing = uiState.isLoading()

        when {
            uiState.hasData() -> showAccount(uiState.requireData())
            uiState.hasError() -> {
                uiState.requireError().message?.let {
                    showError(it)
                }
            }
        }
    }

    private fun showAccount(account: Account) {
        ImageLoader.loadImage(accountImage, account.avatar, centerCrop = true)

        accountUsername.text = getString(R.string.account_username, account.username)
        accountName.text = if (account.name.isEmpty()) {
            getString(R.string.account_name_fallback)
        } else {
            account.name
        }
    }
}