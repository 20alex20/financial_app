package com.example.finances.features.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.models.Currency
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.transactions.navigation.ScreenType
import com.example.finances.features.transactions.domain.models.ShortCategory
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.mappers.toLocalTime
import com.example.finances.features.transactions.ui.models.CreateUpdateViewModelState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Вьюмодель экрана создания/редактирования счета
 */
open class CreateUpdateViewModel @Inject constructor(
    private val transactionsRepo: TransactionsRepo,
    private val convertAmountUseCase: ConvertAmountUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase
) : BaseViewModel() {
    private val _today = LocalDateTime.now()
    private val _screenTypeLatch = CompletableDeferred<ScreenType>()
    private val _transactionIdLatch = CompletableDeferred<Int?>()

    private var _deferredSaving: Deferred<Boolean>? = null
    private var _currency = Currency.RUBLE
    private var _transaction = ShortTransaction(null, 1, 0.0, _today, "")

    private val _sendingError = mutableStateOf(false)
    val sendingError: State<Boolean> = _sendingError

    private val _categories = mutableStateOf(listOf(ShortCategory(
        id = 1,
        name = "Зарплата"
    )))
    val categories: State<List<ShortCategory>> = _categories

    private val _state = mutableStateOf(
        CreateUpdateViewModelState(
            categoryName = "Зарплата",
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
        val screenType = _screenTypeLatch.await()
        return transactionsRepo.getCategories(screenType).let { response ->
            if (response is Response.Success)
                response.data
            else if (screenType == ScreenType.Expenses)
                _defaultExpensesCategories
            else
                _defaultIncomeCategories
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
        val transactionId = _transactionIdLatch.await()
        if (transactionId != null) when (
            val response = transactionsRepo.getTransaction(transactionId)
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
                val response = transactionsRepo.createUpdateTransaction(_transaction)
                resetLoadingAndError()
                if (response is Response.Success) {
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

    override fun setParams(extras: CreationExtras) {
        if (!_transactionIdLatch.isCompleted && !_screenTypeLatch.isCompleted) {
            _screenTypeLatch.complete(extras[ViewModelParams.Screen] ?: ScreenType.Expenses)
            _transactionIdLatch.complete(extras[ViewModelParams.TransactionId])
        }
    }

    init {
        reloadData()
    }

    companion object {
        private const val MAX_COMMENT_LENGTH = 64

        private val _defaultExpensesCategories = listOf(ShortCategory(1, "Зарплата"))
        private val _defaultIncomeCategories = listOf(ShortCategory(7, "Жильё"))
    }
}
