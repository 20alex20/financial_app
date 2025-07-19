package com.example.finances.features.account.data.mappers

import com.example.finances.core.utils.models.Currency
import com.example.finances.features.account.data.models.AccountEntity
import com.example.finances.features.account.domain.models.ShortAccount

fun AccountEntity.toShortAccount() = ShortAccount(
    name = name,
    balance = balance,
    currency = Currency.valueOf(currency)
)
