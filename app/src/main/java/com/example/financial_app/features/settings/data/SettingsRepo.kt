package com.example.financial_app.features.settings.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.financial_app.R
import com.example.financial_app.features.settings.domain.models.Setting

object SettingsRepo {
    @Composable
    fun getSettings(): List<Setting> {
        return listOf(
            Setting(stringResource(R.string.dark_theme), { }, withSwitch = true),
            Setting(stringResource(R.string.main_color), { }),
            Setting(stringResource(R.string.sounds), { }),
            Setting(stringResource(R.string.haptics), { }),
            Setting(stringResource(R.string.code_password), { }),
            Setting(stringResource(R.string.synchronization), { }),
            Setting(stringResource(R.string.language), { }),
            Setting(stringResource(R.string.about_program), { })
        )
    }
}
