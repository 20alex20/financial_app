package com.example.finances.feature.settings.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.settings.domain.models.VibrationDuration
import com.example.finances.feature.settings.domain.repository.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class VibrationViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : BaseViewModel() {
    private val _vibrationDuration = mutableStateOf(settingsRepo.loadVibrationDuration())
    val vibrationDuration: State<VibrationDuration> = _vibrationDuration

    fun setVibrationDuration(duration: VibrationDuration) {
        _vibrationDuration.value = duration
    }

    fun saveVibrationDuration(): Boolean {
        return settingsRepo.saveVibrationDuration(_vibrationDuration.value).also { success ->
            if (!success)
                setError()
        }
    }

    fun resetError() = resetLoadingAndError()

    override suspend fun loadData(scope: CoroutineScope) = resetLoadingAndError()

    override fun setViewModelParams(extras: CreationExtras) {}
}
