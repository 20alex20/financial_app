package com.example.finances.core.utils.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel

@Immutable
fun interface ViewModelProvider<T : ViewModel> {
    fun get(): T
}
