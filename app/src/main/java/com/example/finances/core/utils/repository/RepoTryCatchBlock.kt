package com.example.finances.core.utils.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

private const val RETRY_TIMES = 3
private const val TIMER = 2000L
private val HTTP_ERRORS_WITH_RETRY = listOf(
    HttpURLConnection.HTTP_NOT_FOUND,
    HttpURLConnection.HTTP_INTERNAL_ERROR,
    HttpURLConnection.HTTP_GATEWAY_TIMEOUT
)

suspend fun <T> repoTryCatchBlock(f: suspend () -> T): Response<T> = withContext(Dispatchers.IO) {
    repeat (RETRY_TIMES + 1) { i ->
        try {
            val res = f()
            return@withContext Response.Success(res)
        } catch (e: IOException) {
            if (i == RETRY_TIMES)
                return@withContext Response.Failure(e)
        } catch (e: HttpException) {
            if (i == RETRY_TIMES || e.code() !in HTTP_ERRORS_WITH_RETRY)
                return@withContext Response.Failure(e)
        } catch (_: Exception) {
            return@withContext Response.Failure(UnexpectedException())
        }
        delay(TIMER)
    }
    Response.Failure(UnexpectedException())
}
