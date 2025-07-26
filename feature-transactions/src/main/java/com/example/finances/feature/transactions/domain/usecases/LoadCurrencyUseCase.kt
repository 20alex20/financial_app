package com.example.finances.feature.transactions.domain.usecases

import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.models.Currency
import com.example.finances.feature.transactions.di.TransactionsScope
import com.example.finances.feature.transactions.domain.repository.TransactionsRepo
import javax.inject.Inject

/**
 * Юзкейс для загрузки валюты счета
 */
@TransactionsScope
class LoadCurrencyUseCase @Inject constructor(private val transactionsRepo: TransactionsRepo) {
    suspend operator fun invoke(): Currency {
        return transactionsRepo.getCurrency().let { response ->
            if (response is Response.Success) response.data else Currency.RUBLE
        }
    }
}
