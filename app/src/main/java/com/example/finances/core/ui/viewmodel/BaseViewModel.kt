package com.example.finances.core.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finances.core.buses.ReloadEvent
import com.example.finances.core.buses.ReloadEventBus
import kotlinx.coroutines.Job
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

    protected suspend fun sendReloadEvent(reloadEvent: ReloadEvent) {
        ReloadEventBus.send(reloadEvent)
    }

    protected open suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {}

    protected fun observeReloadEvents() = viewModelScope.launch {
        ReloadEventBus.events.collect { reloadEvent ->
            try {
                handleReloadEvent(reloadEvent)
            } catch (_: Exception) {
            }
        }
    }
}
