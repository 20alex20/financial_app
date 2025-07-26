package com.example.finances.feature.account.data.mappers

import com.example.finances.feature.account.data.models.AccountEntity
import com.example.finances.feature.account.domain.models.Account
import com.example.finances.feature.account.domain.models.ShortAccount

fun Account.toAccountEntity() = AccountEntity(
    id = id,
    name = name,
    balance = balance,
    currency = currency.name,
    isSynced = true
)

fun ShortAccount.toAccountEntity(id: Int) = AccountEntity(
    id = id,
    name = name,
    balance = balance,
    currency = currency.name,
    isSynced = false
)
