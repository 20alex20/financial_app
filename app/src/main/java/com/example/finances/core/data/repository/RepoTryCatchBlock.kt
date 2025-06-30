package com.example.finances.core.data.repository

import com.example.finances.core.data.network.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException

private const val RETRY_TIMES = 3
private const val TIMER = 2000L
private const val ERROR_MESSAGE = "Data loading error"

suspend fun <T> repoTryCatchBlock(f: suspend () -> T): Response<T> = withContext(Dispatchers.IO) {
    repeat(RETRY_TIMES + 1) { i ->
        try {
            val res = f()
            return@withContext Response.Success(res)
        } catch (e: IOException) {
            if (i != RETRY_TIMES)
                delay(TIMER)
            else
                return@withContext Response.Failure(e)
        } catch (e: NoAccountException) {
            return@withContext Response.Failure(e)
        } catch (_: Exception) {
            return@repeat
        }
    }
    return@withContext Response.Failure(IOException(ERROR_MESSAGE))
}
