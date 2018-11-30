package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.model.AccountModel
import com.illiarb.tmdblcient.core.entity.Account
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountMapper @Inject constructor() : Mapper<AccountModel, Account> {

    override fun map(from: AccountModel): Account =
        Account(
            from.id,
            from.name,
            from.username,
            from.avatar.gravatar.hash
        )
}