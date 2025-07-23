package com.example.finances.feature.account.data.mappers

import com.example.finances.feature.account.data.models.AccountUpdateRequest
import com.example.finances.feature.account.domain.models.ShortAccount

fun ShortAccount.toAccountUpdateRequest() = AccountUpdateRequest (
    name = name,
    balance = String.format(null, "%.2f", balance),
    currency = currency.shortName
)
