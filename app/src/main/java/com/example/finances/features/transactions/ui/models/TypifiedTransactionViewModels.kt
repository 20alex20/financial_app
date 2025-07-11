package com.example.finances.features.transactions.ui.models

import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.features.transactions.ui.CreateUpdateViewModel
import com.example.finances.features.transactions.ui.ExpensesIncomeViewModel
import com.example.finances.features.transactions.ui.HistoryViewModel
import javax.inject.Inject

class ExpensesViewModel @Inject constructor(
    transactionsRepo: TransactionsRepo,
    convertAmountUseCase: ConvertAmountUseCase,
    loadCurrencyUseCase: LoadCurrencyUseCase
) : ExpensesIncomeViewModel(
    isIncome = false,
    transactionsRepo = transactionsRepo,
    convertAmountUseCase = convertAmountUseCase,
    loadCurrencyUseCase = loadCurrencyUseCase
)

class IncomeViewModel @Inject constructor(
    transactionsRepo: TransactionsRepo,
    convertAmountUseCase: ConvertAmountUseCase,
    loadCurrencyUseCase: LoadCurrencyUseCase
) : ExpensesIncomeViewModel(
    isIncome = true,
    transactionsRepo = transactionsRepo,
    convertAmountUseCase = convertAmountUseCase,
    loadCurrencyUseCase = loadCurrencyUseCase
)

class ExpensesHistoryViewModel @Inject constructor(
    transactionsRepo: TransactionsRepo,
    convertAmountUseCase: ConvertAmountUseCase,
    loadCurrencyUseCase: LoadCurrencyUseCase
) : HistoryViewModel(
    isIncome = false,
    transactionsRepo = transactionsRepo,
    convertAmountUseCase = convertAmountUseCase,
    loadCurrencyUseCase = loadCurrencyUseCase
)

class IncomeHistoryViewModel @Inject constructor(
    transactionsRepo: TransactionsRepo,
    convertAmountUseCase: ConvertAmountUseCase,
    loadCurrencyUseCase: LoadCurrencyUseCase
) : HistoryViewModel(
    isIncome = true,
    transactionsRepo = transactionsRepo,
    convertAmountUseCase = convertAmountUseCase,
    loadCurrencyUseCase = loadCurrencyUseCase
)

class ExpensesCreateUpdateViewModel @Inject constructor(
    transactionId: Int?,
    transactionsRepo: TransactionsRepo,
    convertAmountUseCase: ConvertAmountUseCase,
    loadCurrencyUseCase: LoadCurrencyUseCase
) : CreateUpdateViewModel(
    isIncome = false,
    transactionId = transactionId,
    transactionsRepo = transactionsRepo,
    convertAmountUseCase = convertAmountUseCase,
    loadCurrencyUseCase = loadCurrencyUseCase
)

class IncomeCreateUpdateViewModel @Inject constructor(
    transactionId: Int?,
    transactionsRepo: TransactionsRepo,
    convertAmountUseCase: ConvertAmountUseCase,
    loadCurrencyUseCase: LoadCurrencyUseCase
) : CreateUpdateViewModel (
    isIncome = true,
    transactionId = transactionId,
    transactionsRepo = transactionsRepo,
    convertAmountUseCase = convertAmountUseCase,
    loadCurrencyUseCase = loadCurrencyUseCase
)
