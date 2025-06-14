package com.example.financial_app.features.expenses.domain.repo

import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.expenses.domain.models.RepoExpense
import com.example.financial_app.features.expenses.domain.models.ExpensesRepoData

class ExpensesRepoLoader {
    fun loadExpensesData(): ExpensesRepoData = ExpensesRepoData(
        436558.00,
        Currency.RUBLE,
        listOf(
            RepoExpense(0, "Аренда квартиры", "\uD83C\uDFE1", 100000.00, null),
            RepoExpense(1, "Одежда", "\uD83D\uDC57", 100000.00, null),
            RepoExpense(2, "На собачку", "\uD83D\uDC36", 100000.00, "Джек"),
            RepoExpense(3, "На собачку", "\uD83D\uDC36", 100000.00, "Энни"),
            RepoExpense(4, "Ремонт квартиры", "РК", 100000.00, null),
            RepoExpense(5, "Продукты", "\uD83C\uDF6D", 100000.00, null),
            RepoExpense(6, "Спортзал", "\uD83C\uDFCB\uFE0F", 100000.00, null),
            RepoExpense(7, "Медицина", "\uD83D\uDC8A", 100000.00, null)
        )
    )
}
