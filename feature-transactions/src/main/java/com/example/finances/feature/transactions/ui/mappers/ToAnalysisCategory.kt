package com.example.finances.feature.transactions.ui.mappers

import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.utils.models.Currency
import com.example.finances.feature.transactions.domain.models.AnalysisGroup
import com.example.finances.feature.transactions.ui.models.AnalysisCategory
import kotlin.math.roundToInt

fun AnalysisGroup.toAnalysisCategory(
    convertAmountUseCase: ConvertAmountUseCase,
    currency: Currency
) = AnalysisCategory(
    name = name,
    emoji = emoji,
    percent = if (percent < 1.0) "<1 %" else "${percent.roundToInt()} %",
    amount = convertAmountUseCase(amount, currency)
)
