package com.example.finances.features.account.data.mappers

import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.account.data.models.AccountResponse
import com.example.finances.features.account.domain.models.Account

fun AccountResponse.toAccount() = Account(
    id = this.id,
    balance = this.balance.toDouble(),
    currency = Currency.parseStr(this.currency)
)
