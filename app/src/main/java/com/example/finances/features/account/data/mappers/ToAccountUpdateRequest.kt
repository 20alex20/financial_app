package com.example.finances.features.account.data.mappers

import com.example.finances.features.account.data.models.AccountUpdateRequest
import com.example.finances.features.account.domain.models.Account

fun Account.toAccountUpdateRequest() = AccountUpdateRequest (
    name = name,
    balance = String.format(null, "%.2f", balance),
    currency = currency.shortName
)
