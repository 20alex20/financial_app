package com.example.finances.features.account.ui.models

import androidx.compose.ui.graphics.painter.Painter
import com.example.finances.core.domain.models.Currency

data class CurrencyModalItem(
    val obj: Currency,
    val name: String,
    val icon: Painter
)
