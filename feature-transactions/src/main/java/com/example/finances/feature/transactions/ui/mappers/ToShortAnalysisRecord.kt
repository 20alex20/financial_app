package com.example.finances.feature.transactions.ui.mappers

import com.example.finances.core.charts.analysis.models.ShortAnalysisRecord
import com.example.finances.feature.transactions.domain.models.AnalysisGroup

fun AnalysisGroup.toShortAnalysisRecord() = ShortAnalysisRecord(
    name = name,
    percent = percent
)
