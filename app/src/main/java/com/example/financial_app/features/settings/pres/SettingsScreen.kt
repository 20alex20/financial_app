package com.example.financial_app.features.settings.pres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financial_app.R
import com.example.financial_app.features.settings.data.SettingsRepo
import com.example.financial_app.ui.components.Header
import com.example.financial_app.ui.components.ListItem
import com.example.financial_app.ui.components.ListItemHeight
import com.example.financial_app.ui.components.Trail

@Composable
fun Settings(vm: SettingsViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(stringResource(R.string.settings))

        val settings = SettingsRepo.getSettings()
        vm.createSwitchesChecked(settings.size)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(settings) { index, setting ->
                ListItem(
                    setting.name,
                    height = ListItemHeight.LOW,
                    trail = if (!setting.withSwitch) Trail.DarkArrow(onClick = setting.onClick)
                    else Trail.Custom {
                        Switch(
                            checked = vm.switchesChecked.value[index],
                            onCheckedChange = { vm.changeSwitchChecked(index) },
                            colors = SwitchDefaults.colors(
                                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        )
                    }
                )
            }
        }
    }
}
