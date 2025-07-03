package com.example.finances.features.account.domain.mappers

import com.example.finances.core.data.repository.mappers.toCurrency
import com.example.finances.features.account.data.models.AccountResponse
import com.example.finances.features.account.domain.models.Account

fun AccountResponse.toAccount() = Account(
    id = id,
    name = name,
    balance = balance.toDouble(),
    currency = currency.toCurrency()
)
