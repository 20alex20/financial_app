package com.example.finances.feature.settings.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.settings.domain.repository.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UserPinViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : BaseViewModel() {
    private val _enabled = mutableStateOf(false)
    val enabled: State<Boolean> = _enabled

    private val _userPin = mutableStateOf("")
    val userPin: State<String> = _userPin

    private val _checkingUserPin = mutableStateOf("")
    val checkingUserPin: State<String> = _checkingUserPin

    private val _errorType = mutableStateOf(0)
    val errorType: State<Int?> = _errorType

    fun changeEnabled() {
        _errorType.value = 0
        _enabled.value = !_enabled.value
    }

    fun setUserPin(pin: String) {
        _errorType.value = 0
        if (pin.length <= 4 && pin.isDigitsOnly())
            _userPin.value = pin
    }

    fun setCheckingUserPin(pin: String) {
        _errorType.value = 0
        if (pin.length <= 4 && pin.isDigitsOnly())
            _checkingUserPin.value = pin
    }

    fun savePrimaryColor(): Boolean {
        return if (!_enabled.value) {
            settingsRepo.saveUserPin(null).also { if (!it) _errorType.value = 3 }
        } else if (_userPin.value.length != 4) {
            _errorType.value = 1
            false
        } else if (_userPin.value != _checkingUserPin.value) {
            _errorType.value = 2
            false
        } else {
            settingsRepo.saveUserPin(userPin.value).also { if (!it) _errorType.value = 3 }
        }
    }

    override suspend fun loadData(scope: CoroutineScope) = resetLoadingAndError()

    override fun setViewModelParams(extras: CreationExtras) {}
}
