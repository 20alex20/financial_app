package com.example.finances.features.transactions.data.models

/**
 * Исключение для ошибки загрузки данных счета
 */
class AccountLoadingException(message: String) : Exception(message)
