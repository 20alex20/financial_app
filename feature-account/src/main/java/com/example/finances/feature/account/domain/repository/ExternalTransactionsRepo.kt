package com.example.finances.feature.account.domain.repository

interface ExternalTransactionsRepo {
    suspend fun getCurrentMonthDifferences(): List<Double>
}
