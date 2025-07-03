package com.example.finances.core.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

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

    protected abstract fun loadData(): Job

    fun reloadData() {
        _loadedJob?.cancel()
        setLoading()
        _loadedJob = loadData()
    }
}
