package com.example.finances.feature.categories.navigation

sealed class CategoriesNavRoutes(val route: String) {
    object Categories : CategoriesNavRoutes("categories")
} 