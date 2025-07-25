package com.example.finances.feature.settings.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.settings.domain.models.ThemeMode
import com.example.finances.feature.settings.domain.repository.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : BaseViewModel() {
    private val _themeMode = mutableStateOf(settingsRepo.loadTheme())
    val themeMode: State<ThemeMode> = _themeMode

    fun changeThemeMode() {
        val newThemeMode = if (_themeMode.value.darkTheme) ThemeMode.LIGHT else ThemeMode.DARK
        if (settingsRepo.saveTheme(newThemeMode))
            _themeMode.value = newThemeMode
    }

    override suspend fun loadData(scope: CoroutineScope) = resetLoadingAndError()

    override fun setViewModelParams(extras: CreationExtras) {}
}
