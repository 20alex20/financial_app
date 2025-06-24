package com.example.financial_app.common.code

import com.example.financial_app.common.models.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> repoTryCatchBlock(func: suspend () -> T): Flow<Response<T>> = flow {
    val repeatTimes = 3
    val timer = 2000L

    for (i in 0..repeatTimes) {
        try {
            if (i == 0)
                emit(Response.Loading)
            val res = func()
            emit(Response.Success(res))
            break
        } catch (e: Exception) {
            if (i == 0)
                emit(Response.Failure(e))
            delay(timer)
        }
    }
}
