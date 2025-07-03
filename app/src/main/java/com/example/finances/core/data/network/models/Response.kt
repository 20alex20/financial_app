package com.example.finances.core.data.network.models

/**
 * Запечатанный интерфейс со всеми типами возвращаемых из data-слоя в ui-слой результатов загрузки
 */
sealed interface Response<out T> {
    /**
     * Успешная загрузка + результат загрузки
     */
    data class Success<out T>(
        val data: T
    ) : Response<T>

    /**
     * Неудачная загрузка + тип ошибки
     */
    data class Failure(
        val e: Exception
    ) : Response<Nothing>
}
