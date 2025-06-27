package com.example.financial_app.common.code

import com.example.financial_app.common.models.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> repoTryCatchBlock(func: suspend () -> T): Flow<Response<T>> = flow {
    val retryTimes = 3
    val timer = 2000L

    emit(Response.Loading)
    repeat (retryTimes + 1) { i ->
        try {
            val res = func()
            emit(Response.Success(res))
            return@repeat
        } catch (e: Exception) {
            if (i == 0)
                emit(Response.Failure(e))
            delay(timer)
        }
    }
}
