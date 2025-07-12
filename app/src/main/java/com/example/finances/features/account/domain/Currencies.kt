package com.example.finances.features.account.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.finances.R
import com.example.finances.core.utils.models.Currency
import com.example.finances.core.ui.components.models.SheetItem

@Composable
fun currencySheetItems() = listOf(
    SheetItem(
        obj = Currency.RUBLE,
        name = stringResource(R.string.ruble),
        icon = painterResource(R.drawable.ruble)
    ),
    SheetItem(
        obj = Currency.DOLLAR,
        name = stringResource(R.string.dollar),
        icon = painterResource(R.drawable.dollar)
    ),
    SheetItem(
        obj = Currency.EURO,
        name = stringResource(R.string.euro),
        icon = painterResource(R.drawable.euro)
    )
)
