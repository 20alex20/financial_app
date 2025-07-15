package com.example.finances.features.account.data.extensions

/**
 * Исключение для ошибки загрузки ID счета
 */
class AccountIdLoadingException(message: String = ACCOUNT_ID_LOADING_ERROR) : Exception(message) {
    companion object {
        private const val ACCOUNT_ID_LOADING_ERROR = "Account id loading error"
    }
}
