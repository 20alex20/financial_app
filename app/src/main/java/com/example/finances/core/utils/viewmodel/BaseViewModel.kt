package com.example.finances.core.utils.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Базовый класс для всех вьюмоделей приложения
 */
abstract class BaseViewModel : ViewModel() {
    private var _loadedJob: Job? = null

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf(false)
    val error: State<Boolean> = _error

    protected fun setLoading() {
        _error.value = false
        _loading.value = true
    }

    protected fun setError() {
        _loading.value = false
        _error.value = true
    }

    protected fun resetLoadingAndError() {
        _loading.value = false
        _error.value = false
    }

    protected abstract suspend fun loadData()

    fun reloadData() {
        _loadedJob?.cancel()
        _loadedJob = viewModelScope.launch {
            setLoading()
            try {
                loadData()
            } catch (_: Exception) {
                setError()
            }
        }
    }

    abstract fun setParams(extras: CreationExtras)

    protected open suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {}

    protected fun observeReloadEvents() = viewModelScope.launch {
        events.collect { reloadEvent ->
            try {
                handleReloadEvent(reloadEvent)
            } catch (_: Exception) {
            }
        }
    }

    companion object ReloadEventBus {
        private val _events = MutableSharedFlow<ReloadEvent>(replay = 0)
        val events = _events.asSharedFlow()

        suspend fun send(event: ReloadEvent) {
            _events.emit(event)
        }
    }
}
