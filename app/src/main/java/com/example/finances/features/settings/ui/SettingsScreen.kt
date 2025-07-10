package com.example.finances.features.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.finances.R
import com.example.finances.core.ui.components.TopBar
import com.example.finances.features.settings.domain.models.Settings

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    var settings by remember { mutableStateOf(viewModel.getSettings()) }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.settings))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Switch(
                checked = settings.isDarkTheme,
                onCheckedChange = { isChecked ->
                    settings = settings.copy(isDarkTheme = isChecked)
                    viewModel.updateSettings(settings)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.dark_theme),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Switch(
                checked = settings.isNotificationsEnabled,
                onCheckedChange = { isChecked ->
                    settings = settings.copy(isNotificationsEnabled = isChecked)
                    viewModel.updateSettings(settings)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
