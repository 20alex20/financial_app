package com.example.finances.core.utils.repository

/**
 * Исключение для непредвиденных ошибок
 */
class UnexpectedException(message: String = UNEXPECTED_LOADING_ERROR) : Exception(message) {
    companion object {
        private const val UNEXPECTED_LOADING_ERROR = "Unexpected data loading error"
    }
}
