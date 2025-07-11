package com.example.finances.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.ui.theme.FinancesTheme
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory

@Composable
fun MainScreen(
    viewModelFactory: ViewModelProvider.Factory,
    appNavigationCoordinator: AppNavigationCoordinator
) {
    FinancesTheme(dynamicColor = false) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
                AppNavigation(
                    appNavigationCoordinator = appNavigationCoordinator,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, padding.calculateBottomPadding())
                )
            }
        }
    }
}
