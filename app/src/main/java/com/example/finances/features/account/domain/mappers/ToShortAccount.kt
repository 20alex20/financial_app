package com.example.finances.features.account.domain.mappers

import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.account.domain.models.ShortAccount

fun Account.toShortAccount() = ShortAccount(
    name = name,
    balance = balance,
    currency = currency
)
