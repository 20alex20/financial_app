package com.example.finances.common.code

import com.example.finances.common.models.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

const val RETRY_TIMES = 3
const val TIMER = 2000L
const val ERROR_MESSAGE = "Data loading error"

fun <T> repoTryCatchBlock(func: suspend () -> T): Flow<Response<T>> = flow {
    emit(Response.Loading)
    repeat (RETRY_TIMES + 1) { i ->
        try {
            val res = func()
            emit(Response.Success(res))
            return@repeat
        } catch (_: Exception) {
            if (i == 0)
                emit(Response.Failure(IOException(ERROR_MESSAGE)))
            delay(TIMER)
        }
    }
}
