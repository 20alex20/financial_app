package com.example.financial_app.features.history.domain.repo

import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.history.domain.models.HistoryRepoData
import com.example.financial_app.features.history.domain.models.RepoHistoryRecord

class HistoryRepoLoader {
    fun loadHistoryData(): HistoryRepoData = HistoryRepoData(
        "Февраль 2025",
        "23:41",
        125868.00,
        Currency.RUBLE,
        listOf(
            RepoHistoryRecord(0, "Ремонт квартиры", "РК", "22:01", 100000.00, "Ремонт - фурнитура для дверей"),
            RepoHistoryRecord(1, "На собачку", "\uD83D\uDC36", "22:01", 100000.00, null),
            RepoHistoryRecord(2, "На собачку", "\uD83D\uDC36", "22:01", 100000.00, null),
            RepoHistoryRecord(3, "На собачку", "\uD83D\uDC36", "22:01", 100000.00, null),
            RepoHistoryRecord(4, "На собачку", "\uD83D\uDC36", "22:01", 100000.00, null)
        )
    )
}
