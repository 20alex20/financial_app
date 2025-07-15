package com.example.finances.features.transactions.data.extensions

/**
 * Исключение для ошибки загрузки данных счета
 */
class AccountLoadingException(message: String = ACCOUNT_LOADING_ERROR) : Exception(message) {
    companion object {
        private const val ACCOUNT_LOADING_ERROR = "Account data loading error"
    }
}
