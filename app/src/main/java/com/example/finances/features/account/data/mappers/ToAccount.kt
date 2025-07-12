package com.example.finances.features.account.data.mappers

import com.example.finances.features.account.data.models.AccountResponse
import com.example.finances.features.account.domain.models.Account

fun AccountResponse.toAccount() = Account(
    id = id,
    name = name,
    balance = balance.toDouble(),
    currency = currency.toCurrency()
)
