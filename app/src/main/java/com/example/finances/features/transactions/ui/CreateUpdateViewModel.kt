package com.example.finances.features.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.utils.models.Currency
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.transactions.domain.models.ShortCategory
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.mappers.toLocalTime
import com.example.finances.features.transactions.ui.models.CreateUpdateViewModelState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Вьюмодель экрана создания/редактирования счета
 */
open class CreateUpdateViewModel(
    private val isIncome: Boolean,
    private val transactionsRepo: TransactionsRepo,
    private val convertAmountUseCase: ConvertAmountUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase
) : BaseViewModel() {
    private val _today = LocalDateTime.now()
    private val _defaultTransactionId = if (isIncome) 1 else 7
    private val _defaultTransactionCategoryName = if (isIncome) "Жильё" else "Зарплата"
    private val _transactionId = if (isIncome) {
        TransactionsRepo.incomeTransactionId
    } else {
        TransactionsRepo.expenseTransactionId
    }

    private var _deferredSaving: Deferred<Boolean>? = null
    private var _currency = Currency.RUBLE
    private var _transaction = ShortTransaction(null, _defaultTransactionId, 0.0, _today, "")

    private val _sendingError = mutableStateOf(false)
    val sendingError: State<Boolean> = _sendingError

    private val _categories = mutableStateOf(listOf(ShortCategory(
        id = _defaultTransactionId,
        name = _defaultTransactionCategoryName
    )))
    val categories: State<List<ShortCategory>> = _categories

    private val _state = mutableStateOf(
        CreateUpdateViewModelState(
            categoryName = _defaultTransactionCategoryName,
            amount = "0 ₽",
            date = _today.format(DateTimeFormatters.date),
            time = _today.format(DateTimeFormatters.time),
            comment = ""
        )
    )
    val state: State<CreateUpdateViewModelState> = _state

    fun updateAmount(newAmount: String) {
        val amount = convertAmountUseCase(newAmount)
        _transaction = _transaction.copy(amount = amount)
        _state.value = _state.value.copy(amount = convertAmountUseCase(amount, _currency))
    }

    fun updateDate(newDate: LocalDate) {
        val dateTime = newDate.atTime(_transaction.dateTime.toLocalTime())
        _transaction = _transaction.copy(dateTime = dateTime)
        _state.value = _state.value.copy(date = newDate.format(DateTimeFormatters.date))
    }

    fun updateTime(newTime: String) {
        val time = newTime.toLocalTime() ?: return
        val dateTime = _transaction.dateTime.toLocalDate().atTime(time)
        _transaction = _transaction.copy(dateTime = dateTime)
        _state.value = _state.value.copy(time = time.format(DateTimeFormatters.time))
    }

    fun updateComment(newComment: String) {
        val comment = newComment.take(MAX_COMMENT_LENGTH)
        _transaction = _transaction.copy(comment = comment)
        _state.value = _state.value.copy(comment = comment)
    }

    fun updateCategory(categoryId: Int) {
        val category = _categories.value.find { it.id == categoryId }
        if (category == null)
            return
        _transaction = _transaction.copy(categoryId = categoryId)
        _state.value = _state.value.copy(categoryName = category.name)
    }

    private suspend fun loadCategories(): List<ShortCategory> {
        return transactionsRepo.getCategories(isIncome).let { response ->
            if (response is Response.Success)
                response.data
            else
                listOf(ShortCategory(_defaultTransactionId, _defaultTransactionCategoryName))
        }
    }

    private fun processSuccessLoading() {
        resetLoadingAndError()
        val category = _categories.value.find { it.id == _transaction.categoryId }
        if (category == null)
            setError()
        else _state.value = CreateUpdateViewModelState(
            categoryName = category.name,
            amount = convertAmountUseCase(_transaction.amount, _currency),
            date = _transaction.dateTime.format(DateTimeFormatters.date),
            time = _transaction.dateTime.format(DateTimeFormatters.time),
            comment = _transaction.comment
        )
    }

    private fun processEmptyStart() {
        resetLoadingAndError()
        _transaction = _transaction.copy(categoryId = _categories.value.first().id)
        _state.value = _state.value.copy(categoryName = _categories.value.first().name)
    }

    override suspend fun loadData() {
        val asyncCurrency = viewModelScope.async { loadCurrencyUseCase() }
        val asyncCategories = viewModelScope.async { loadCategories() }
        if (_transactionId != null) when (
            val response = transactionsRepo.getTransaction(_transactionId)
        ) {
            is Response.Failure -> setError()
            is Response.Success -> {
                _transaction = response.data
                _currency = asyncCurrency.await()
                _categories.value = asyncCategories.await()
                processSuccessLoading()
            }
        } else {
            _currency = asyncCurrency.await()
            _categories.value = asyncCategories.await()
            processEmptyStart()
        }
    }

    fun saveChanges(): Deferred<Boolean> {
        _deferredSaving?.cancel()
        return viewModelScope.async {
            _sendingError.value = false
            setLoading()
            try {
                val response = if (_transaction.id == null)
                    transactionsRepo.createTransaction(_transaction)
                else
                    transactionsRepo.updateTransaction(_transaction)
                resetLoadingAndError()
                if (response is Response.Success && response.data.amount == _transaction.amount &&
                    response.data.categoryId == _transaction.categoryId
                ) {
                    send(ReloadEvent.TransactionCreatedUpdated)
                    return@async true
                }
            } catch (_: Exception) {
                resetLoadingAndError()
            }
            _sendingError.value = true
            false
        }.also { _deferredSaving = it }
    }

    init {
        reloadData()
    }

    companion object {
        private const val MAX_COMMENT_LENGTH = 64
    }
}
