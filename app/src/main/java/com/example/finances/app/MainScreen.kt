package com.example.finances.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finances.core.navigation.AppNavigation
import com.example.finances.core.ui.theme.FinancesTheme

@Composable
fun MainScreen() {
    FinancesTheme(dynamicColor = false) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavigation(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, innerPadding.calculateBottomPadding())
            )
        }
    }
}
