package com.example.financial_app.features.check.domain.repo

import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.check.domain.models.CheckRepoData

class CheckRepoLoader {
    fun loadCheckData(): CheckRepoData = CheckRepoData(
        -670000.00,
        Currency.RUBLE
    )
}
