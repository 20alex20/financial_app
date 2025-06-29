package com.example.finances.core.data.repository

import com.example.finances.core.data.network.models.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

private const val RETRY_TIMES = 3
private const val TIMER = 2000L
private const val ERROR_MESSAGE = "Data loading error"

fun <T> repoTryCatchBlock(func: suspend () -> T): Flow<Response<T>> = flow {
    emit(Response.Loading)
    repeat (RETRY_TIMES + 1) { i ->
        try {
            val res = func()
            emit(Response.Success(res))
            return@repeat
        } catch (_: Exception) {
            if (i == RETRY_TIMES)
                emit(Response.Failure(IOException(ERROR_MESSAGE)))
            else
                delay(TIMER)
        }
    }
}
