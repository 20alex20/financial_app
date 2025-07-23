package com.example.finances.feature.transactions.data.extensions

/**
 * Исключение для ошибки загрузки данных статей
 */
class CategoriesLoadingException(message: String = CATEGORIES_LOADING_ERROR) : Exception(message) {
    companion object {
        const val CATEGORIES_LOADING_ERROR = "Categories data loading error"
    }
}
