package com.example.finances.features.transactions.domain.extensions

/**
 * Исключение для ошибки загрузки данных статей
 */
class CategoriesLoadingException(message: String) : Exception(message)
