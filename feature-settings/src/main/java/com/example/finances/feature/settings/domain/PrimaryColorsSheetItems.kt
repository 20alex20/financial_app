package com.example.finances.feature.settings.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.finances.core.ui.components.SheetItem
import com.example.finances.feature.settings.R
import com.example.finances.feature.settings.domain.models.PrimaryColor

@Composable
fun primaryColorsSheetItems() = listOf(
    SheetItem(
        obj = PrimaryColor.GREEN,
        name = stringResource(R.string.green),
        icon = null
    ),
    SheetItem(
        obj = PrimaryColor.PURPLE,
        name = stringResource(R.string.purple),
        icon = null
    ),
    SheetItem(
        obj = PrimaryColor.BLUE,
        name = stringResource(R.string.blue),
        icon = null
    )
)
