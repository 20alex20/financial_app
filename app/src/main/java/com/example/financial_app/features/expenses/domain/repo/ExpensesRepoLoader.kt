package com.example.financial_app.features.expenses.domain.repo

import com.example.financial_app.features.expenses.domain.models.Expense
import com.example.financial_app.features.expenses.domain.models.ExpensesRepoData

class ExpensesRepoLoader {
    fun loadExpensesData(): ExpensesRepoData = ExpensesRepoData(
        "436 558 ₽",
        listOf(
            Expense(0, "Аренда квартиры", "100 000 ₽", "\uD83C\uDFE1", null),
            Expense(0, "Одежда", "100 000 ₽", "\uD83D\uDC57", null),
            Expense(0, "На собачку", "100 000 ₽", "\uD83D\uDC36", "Джек"),
            Expense(0, "На собачку", "100 000 ₽", "\uD83D\uDC36", "Энни"),
            Expense(0, "Ремонт квартиры", "100 000 ₽", null, null),
            Expense(0, "Продукты", "100 000 ₽", "\uD83C\uDF6D", null),
            Expense(0, "Спортзал", "100 000 ₽", "\uD83C\uDFCB\uFE0F", null),
            Expense(0, "Медицина", "100 000 ₽", "\uD83D\uDC8A", null)
        )
    )
}
