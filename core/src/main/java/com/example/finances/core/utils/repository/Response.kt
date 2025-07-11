package com.example.finances.core.utils.repository

/**
 * Sealed interface with all types of loading results returned from data layer to ui layer
 */
sealed interface Response<out T> {
    /**
     * Successful loading + loading result
     */
    data class Success<out T>(
        val data: T
    ) : Response<T>

    /**
     * Failed loading + error type
     */
    data class Failure(
        val e: Exception
    ) : Response<Nothing>
}
