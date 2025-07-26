package com.example.finances.feature.settings.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.settings.domain.models.PrimaryColor
import com.example.finances.feature.settings.domain.repository.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class PrimaryColorViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : BaseViewModel() {
    private val _primaryColor = mutableStateOf(settingsRepo.loadPrimaryColor())
    val primaryColor: State<PrimaryColor> = _primaryColor

    fun setPrimaryColor(primary: PrimaryColor) {
        _primaryColor.value = primary
    }

    fun savePrimaryColor(): Boolean {
        return settingsRepo.savePrimaryColor(_primaryColor.value).also { success ->
            if (!success)
                setError()
        }
    }

    fun resetError() = resetLoadingAndError()

    override suspend fun loadData(scope: CoroutineScope) = resetLoadingAndError()

    override fun setViewModelParams(extras: CreationExtras) {}
}
