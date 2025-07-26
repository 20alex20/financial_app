package com.example.finances.feature.account.ui.mappers

import com.example.finances.feature.account.domain.models.Account
import com.example.finances.feature.account.domain.models.ShortAccount

fun Account.toShortAccount() = ShortAccount(
    name = name,
    balance = balance,
    currency = currency
)
