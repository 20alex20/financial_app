package com.example.finances.features.account.domain.extensions

/**
 * Исключение для ошибки загрузки данных счета
 */
class AccountLoadingException(message: String) : Exception(message)
