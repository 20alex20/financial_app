package com.example.finances.core.utils.repository

sealed interface Response<out T> {
    data class Success<out T>(
        val data: T
    ) : Response<T>

    data class Failure(
        val e: Exception
    ) : Response<Nothing>
}
