package com.example.finances.app

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.finances.app.navigation.AppNavigation
import com.example.finances.app.navigation.AppNavigationCoordinator
import com.example.finances.core.managers.VibrateUseCase
import com.example.finances.core.ui.components.UserPinScreen
import com.example.finances.core.ui.theme.FinancesTheme
import com.example.finances.core.ui.theme.Green
import com.example.finances.core.utils.models.ThemeParameters
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.feature.settings.domain.repository.ExternalSettingsRepo

@RequiresPermission(Manifest.permission.VIBRATE)
@Composable
fun MainScreen(
    externalSettingsRepo: ExternalSettingsRepo,
    viewModelFactory: ViewModelProvider.Factory,
    appNavigationCoordinator: AppNavigationCoordinator,
    vibrateUseCase: VibrateUseCase
) {
    val userPin = externalSettingsRepo.loadUserPin()
    val userPinScreenOpen = remember { mutableStateOf(true) }
    FinancesTheme(externalSettingsRepo.getThemeParameters()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            if (!userPinScreenOpen.value || userPin == null) CompositionLocalProvider(
                LocalViewModelFactory provides viewModelFactory
            ) {
                AppNavigation(
                    appNavigationCoordinator = appNavigationCoordinator,
                    vibrateUseCase = vibrateUseCase,
                    modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
                )
            } else UserPinScreen(userPin, userPinScreenOpen)
        }
    }
}
