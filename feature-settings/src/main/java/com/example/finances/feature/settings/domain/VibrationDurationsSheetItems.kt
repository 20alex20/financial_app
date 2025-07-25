package com.example.finances.feature.settings.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.finances.core.ui.components.SheetItem
import com.example.finances.feature.settings.R
import com.example.finances.feature.settings.domain.models.VibrationDuration

@Composable
fun vibrationDurationsSheetItems() = listOf(
    SheetItem(
        obj = VibrationDuration.DISABLED,
        name = stringResource(R.string.disabled),
        icon = null
    ),
    SheetItem(
        obj = VibrationDuration.SHORT,
        name = stringResource(R.string.short_duration),
        icon = null
    ),
    SheetItem(
        obj = VibrationDuration.MEDIUM,
        name = stringResource(R.string.normal_duration),
        icon = null
    ),
    SheetItem(
        obj = VibrationDuration.LONG,
        name = stringResource(R.string.long_duration),
        icon = null
    )
)
