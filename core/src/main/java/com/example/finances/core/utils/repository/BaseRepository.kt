package com.example.finances.core.utils.repository

/**
 * Base repository interface that all feature repositories should implement.
 * Provides common error handling and response mapping functionality.
 */
interface BaseRepository {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Response<T> = try {
        Response.Success(apiCall())
    } catch (e: Exception) {
        Response.Error(e)
    }
} 