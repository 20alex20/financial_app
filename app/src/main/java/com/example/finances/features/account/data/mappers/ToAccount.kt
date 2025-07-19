package com.example.finances.features.account.data.mappers

import com.example.finances.core.utils.models.Currency
import com.example.finances.features.account.data.models.AccountResponse
import com.example.finances.features.account.data.models.AccountEntity
import com.example.finances.features.account.domain.models.Account

fun AccountResponse.toAccount() = Account(
    id = id,
    name = name,
    balance = balance.toDouble(),
    currency = Currency.fromShortName(currency)
)

fun AccountEntity.toAccount() = Account(
    id = id,
    name = name,
    balance = balance,
    currency = Currency.valueOf(currency)
)
