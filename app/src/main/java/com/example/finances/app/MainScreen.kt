package com.example.finances.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.finances.app.navigation.AppNavigation
import com.example.finances.app.navigation.AppNavigationCoordinator
import com.example.finances.core.managers.VibrateUseCase
import com.example.finances.core.ui.theme.FinancesTheme
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory

@Composable
fun MainScreen(
    appNavigationCoordinator: AppNavigationCoordinator,
    viewModelFactory: ViewModelProvider.Factory,
    vibrateUseCase: VibrateUseCase
) {
    FinancesTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
                AppNavigation(
                    appNavigationCoordinator = appNavigationCoordinator,
                    vibrateUseCase = vibrateUseCase,
                    modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
                )
            }
        }
    }
}
