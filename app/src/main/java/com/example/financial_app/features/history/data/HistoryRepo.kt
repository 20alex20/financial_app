package com.example.financial_app.features.history.data

import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.history.domain.models.HistoryRecord
import com.example.financial_app.features.history.domain.models.RepoHistoryRecord
import com.example.financial_app.features.history.domain.repo.HistoryRepoLoader

object HistoryRepo {
    private var loaded: Boolean = false
    private val loader: HistoryRepoLoader = HistoryRepoLoader()

    private var startDateTime: String = ""
    private var endDateTime: String = ""
    private var sum: Double = 0.0
    private var currency: Currency = Currency.RUBLE
    private var history: List<RepoHistoryRecord> = listOf()

    private fun load() {
        val data = loader.loadHistoryData()
        startDateTime = data.startDateTime
        endDateTime = data.endDateTime
        sum = data.sum
        currency = data.currency
        history = data.history
        loaded = true
    }

    fun getStartDateTime(): String {
        if (!loaded)
            load()
        return startDateTime
    }

    fun getEndDateTime(): String {
        if (!loaded)
            load()
        return endDateTime
    }

    fun getSum(): String {
        if (!loaded)
            load()
        return getStringAmount(sum, currency)
    }

    fun getHistory(): List<HistoryRecord> {
        if (!loaded)
            load()
        return history.map {
            HistoryRecord(
                it.id,
                it.categoryName,
                it.categoryEmoji,
                it.dateTime,
                getStringAmount(it.amount, currency),
                it.comment
            )
        }
    }
}
