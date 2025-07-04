package com.example.finances.features.transactions.domain

import com.example.finances.core.data.Response
import com.example.finances.core.domain.models.Currency
import com.example.finances.features.transactions.domain.repository.TransactionsRepo

class LoadCurrencyUseCase {
    suspend operator fun invoke(transactionsRepo: TransactionsRepo) : Currency {
        return transactionsRepo.getCurrency().let {
            if (it is Response.Success) it.data else Currency.RUBLE
        }
    }
}
