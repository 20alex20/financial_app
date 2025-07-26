package com.example.finances.feature.transactions.ui.models

import com.example.finances.core.charts.analysis.models.ShortAnalysisRecord

data class AnalysisViewModelState(
    val total: String,
    val analysis: List<AnalysisCategory>,
    val pieChartData: List<ShortAnalysisRecord>
)
