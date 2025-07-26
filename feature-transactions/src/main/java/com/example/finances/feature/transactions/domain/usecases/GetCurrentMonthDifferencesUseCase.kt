package com.example.finances.feature.transactions.domain.usecases

import com.example.finances.core.utils.repository.Response
import com.example.finances.feature.transactions.di.TransactionsScope
import com.example.finances.feature.transactions.domain.models.Transaction
import com.example.finances.feature.transactions.domain.repository.TransactionsRepo
import com.example.finances.feature.transactions.navigation.ScreenType
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@TransactionsScope
class GetCurrentMonthDifferencesUseCase @Inject constructor() {
    suspend operator fun invoke(transactionsRepo: TransactionsRepo): List<Double> {
        val today = LocalDate.now()
        val daysInMonth = YearMonth.from(today).lengthOfMonth()
        val firstDayOfMonth = today.withDayOfMonth(1)
        val lastDayOfMonth = today.withDayOfMonth(daysInMonth)

        val income = transactionsRepo.getTransactions(
            firstDayOfMonth,
            lastDayOfMonth,
            ScreenType.Income
        )
        val expenses = transactionsRepo.getTransactions(
            firstDayOfMonth,
            lastDayOfMonth,
            ScreenType.Expenses
        )
        return List(daysInMonth) { getTotal(income, it + 1) - getTotal(expenses, it + 1) }
    }

    private fun getTotal(response: Response<List<Transaction>>, currentDay: Int): Double {
        return if (response is Response.Success) response.data.filter { transaction ->
            transaction.dateTime.dayOfMonth == currentDay
        }.sumOf { it.amount } else 0.0
    }
}
