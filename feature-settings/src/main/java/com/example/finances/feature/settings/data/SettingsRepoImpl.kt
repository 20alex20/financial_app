package com.example.finances.feature.settings.data

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import androidx.core.content.edit
import com.example.finances.core.managers.VibrateUseCase
import com.example.finances.core.utils.models.ThemeParameters
import com.example.finances.feature.settings.domain.models.VibrationDuration
import com.example.finances.feature.settings.domain.models.PrimaryColor
import com.example.finances.feature.settings.domain.models.ThemeMode
import com.example.finances.feature.settings.di.EncryptedPreferences
import com.example.finances.feature.settings.di.Preferences
import com.example.finances.feature.settings.di.SettingsScope
import com.example.finances.feature.settings.domain.models.AppInfo
import com.example.finances.feature.settings.domain.repository.ExternalSettingsRepo
import com.example.finances.feature.settings.domain.repository.SettingsRepo

@SettingsScope
class SettingsRepoImpl @Inject constructor(
    @Preferences private val preferences: SharedPreferences,
    @EncryptedPreferences private val encryptedPreferences: SharedPreferences,
    private val appInfo: AppInfo,
    private val vibrateUseCase: VibrateUseCase
) : SettingsRepo, ExternalSettingsRepo {
    private val _themeParameters = mutableStateOf(
        ThemeParameters(loadTheme().darkTheme, loadPrimaryColor().color)
    )
    override val themeParameters: State<ThemeParameters> = _themeParameters

    override fun saveTheme(themeMode: ThemeMode): Boolean {
        preferences.edit { putBoolean(DARK_THEME, themeMode.darkTheme) }
        return (themeMode == loadTheme()).also { success ->
            if (success) _themeParameters.value = _themeParameters.value.copy(
                darkTheme = themeMode.darkTheme
            )
        }
    }

    override fun loadTheme(): ThemeMode {
        return if (preferences.getBoolean(DARK_THEME, false)) ThemeMode.DARK else ThemeMode.LIGHT
    }

    override fun savePrimaryColor(primaryColor: PrimaryColor): Boolean {
        saveStringPreference(PRIVATE_COLOR, primaryColor)
        return (primaryColor == loadPrimaryColor()).also { success ->
            if (success) _themeParameters.value = _themeParameters.value.copy(
                primaryColor = primaryColor.color
            )
        }
    }

    override fun loadPrimaryColor() = loadStringPreference<PrimaryColor>(
        PRIVATE_COLOR
    ) ?: PrimaryColor.GREEN

    override fun saveVibrationDuration(vibrationDuration: VibrationDuration): Boolean {
        saveStringPreference(VIBRATION_DURATION, vibrationDuration)
        return (vibrationDuration == loadVibrationDuration()).also { success ->
            if (success)
                vibrateUseCase.setDuration(vibrationDuration.duration)
        }
    }

    override fun loadVibrationDuration(): VibrationDuration {
        return loadStringPreference<VibrationDuration>(
            VIBRATION_DURATION
        ) ?: VibrationDuration.DISABLED
    }

    private fun <T : Enum<T>> saveStringPreference(preferenceName: String, enumValue: T) {
        preferences.edit { putString(preferenceName, enumValue.name) }
    }

    private inline fun <reified T : Enum<T>> loadStringPreference(preferenceName: String): T? {
        val name = preferences.getString(preferenceName, null)
        return name?.let { runCatching { enumValueOf<T>(it) }.getOrNull() }
    }

    override fun saveUserPin(userPin: String?): Boolean {
        encryptedPreferences.edit {
            if (userPin == null) {
                remove(USER_PIN)
            } else {
                putString(USER_PIN, userPin)
            }
        }
        return userPin == loadUserPin()
    }

    override fun loadUserPin(): String? {
        return encryptedPreferences.getString(USER_PIN, null)
    }

    override fun loadAppInfo(): AppInfo {
        return appInfo
    }

    companion object {
        private const val DARK_THEME = "darkTheme"
        private const val PRIVATE_COLOR = "primaryColor"
        private const val VIBRATION_DURATION = "vibrationDuration"
        private const val USER_PIN = "userPin"
    }
}
