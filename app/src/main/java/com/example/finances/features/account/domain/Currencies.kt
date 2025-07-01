package com.example.finances.features.account.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.finances.R
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.account.ui.models.CurrencyModalItem

@Composable
fun currencyModalItems() = listOf(
    CurrencyModalItem(
        obj = Currency.RUBLE,
        name = stringResource(R.string.ruble),
        icon = painterResource(R.drawable.ruble)
    ),
    CurrencyModalItem(
        obj = Currency.DOLLAR,
        name = stringResource(R.string.dollar),
        icon = painterResource(R.drawable.dollar)
    ),
    CurrencyModalItem(
        obj = Currency.EURO,
        name = stringResource(R.string.euro),
        icon = painterResource(R.drawable.euro)
    )
)
