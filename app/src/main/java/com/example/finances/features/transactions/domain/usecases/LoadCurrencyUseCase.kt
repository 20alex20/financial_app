package com.example.finances.features.transactions.domain.usecases

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.models.Currency
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import javax.inject.Inject

/**
 * Юзкейс для загрузки валюты счета
 */
@ActivityScope
class LoadCurrencyUseCase @Inject constructor(private val transactionsRepo: TransactionsRepo) {
    suspend operator fun invoke(): Currency {
        return transactionsRepo.getCurrency().let { response ->
            if (response is Response.Success) response.data else Currency.RUBLE
        }
    }
}
