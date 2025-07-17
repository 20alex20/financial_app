package com.example.finances.core.utils.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

private const val ATTEMPTS_NUMBER = 3
private const val TIMER = 2000L
private val HTTP_ERRORS_WITH_RETRY = listOf(
    HttpURLConnection.HTTP_NOT_FOUND,
    HttpURLConnection.HTTP_INTERNAL_ERROR,
    HttpURLConnection.HTTP_GATEWAY_TIMEOUT
)

suspend fun <T> repoTryCatchBlock(
    isOnline: Boolean,
    f: suspend (Boolean) -> T
): Response<T> = withContext(Dispatchers.IO) {
    var networkExceptionMessage: String? = null
    var attempt = if (isOnline) 0 else ATTEMPTS_NUMBER
    while (attempt <= ATTEMPTS_NUMBER) {
        try {
            val res = f(attempt == ATTEMPTS_NUMBER)
            return@withContext Response.Success(res)
        } catch (e: IOException) {
            networkExceptionMessage = e.message
        } catch (e: HttpException) {
            networkExceptionMessage = e.message
            if (e.code() !in HTTP_ERRORS_WITH_RETRY) {
                attempt = ATTEMPTS_NUMBER
                continue
            }
        } catch (_: Exception) {
            if (attempt < ATTEMPTS_NUMBER)
                attempt = ATTEMPTS_NUMBER - 1
        }
        attempt += 1
        if (attempt < ATTEMPTS_NUMBER)
            delay(TIMER)
    }
    Response.Failure(
        if (networkExceptionMessage != null)
            LocalLoadingWithNetworkException(networkExceptionMessage)
        else
            LocalLoadingWithNetworkException()
    )
}
