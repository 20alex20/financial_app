package com.example.finances.features.transactions.domain

import com.example.finances.core.data.Response
import com.example.finances.core.domain.models.Currency
import com.example.finances.features.transactions.domain.repository.TransactionsRepo

/**
 * Юзкейс для загрузки валюты счета
 */
class LoadCurrencyUseCase(private val transactionsRepo: TransactionsRepo) {
    suspend operator fun invoke() : Currency {
        return transactionsRepo.getCurrency().let { response ->
            if (response is Response.Success)
                response.data
            else
                Currency.RUBLE
        }
    }
}
