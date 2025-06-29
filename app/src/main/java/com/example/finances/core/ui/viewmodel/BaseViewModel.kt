package com.example.finances.core.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf(false)
    val error: State<Boolean> = _error

    protected fun resetLoadingAndError() {
        _loading.value = false
        _error.value = false
    }

    protected fun setLoading() {
        _error.value = false
        _loading.value = true
    }

    protected fun setError() {
        _loading.value = false
        _error.value = true
    }

    abstract fun loadData(): Any?

    init {
        this.loadData()
    }
}
