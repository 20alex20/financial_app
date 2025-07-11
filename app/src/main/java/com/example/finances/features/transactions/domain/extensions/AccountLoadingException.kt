package com.example.finances.features.transactions.domain.extensions

/**
 * Исключение для ошибки загрузки данных счета
 */
class CategoriesLoadingException(message: String) : Exception(message)
