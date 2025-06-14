package com.example.financial_app.features.categories.domain.repo

import com.example.financial_app.features.categories.domain.models.CategoriesRepoData
import com.example.financial_app.features.categories.domain.models.Category

class CategoriesRepoLoader {
    fun loadCategoriesData(): CategoriesRepoData = CategoriesRepoData(
        listOf(
            Category(0, "Аренда квартиры", "\uD83C\uDFE1"),
            Category(1, "Одежда", "\uD83D\uDC57"),
            Category(2, "На собачку", "\uD83D\uDC36"),
            Category(3, "На собачку", "\uD83D\uDC36"),
            Category(4, "Ремонт квартиры", "РК"),
            Category(5, "Продукты", "\uD83C\uDF6D"),
            Category(6, "Спортзал", "\uD83C\uDFCB\uFE0F"),
            Category(7, "Медицина", "\uD83D\uDC8A")
        )
    )
}
