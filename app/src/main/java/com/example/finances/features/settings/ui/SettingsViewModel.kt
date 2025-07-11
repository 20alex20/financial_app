package com.example.finances.features.settings.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State

class SettingsViewModel : ViewModel() {
    private val _switchesChecked = mutableStateOf(listOf<Boolean>())
    val switchesChecked: State<List<Boolean>> = _switchesChecked

    fun changeSwitchChecked(index: Int) {
        if (index < 0 || index >= _switchesChecked.value.size)
            return
        val checked = _switchesChecked.value.toMutableList()
        checked[index] = !checked[index]
        _switchesChecked.value = checked
    }

    fun createSwitchesChecked(size: Int) {
        if (_switchesChecked.value.isEmpty())
            _switchesChecked.value = List(size) { false }
    }
}
