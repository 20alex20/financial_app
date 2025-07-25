package com.example.finances.feature.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ModalSheet
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.feature.settings.R
import com.example.finances.feature.settings.domain.models.PrimaryColor
import com.example.finances.feature.settings.domain.primaryColorsSheetItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryColorScreen(navController: NavController) {
    val vm: PrimaryColorViewModel = viewModel(factory = LocalViewModelFactory.current)

    val coroutineScope = rememberCoroutineScope()
    val isSheetOpen = remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = stringResource(R.string.settings),
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.cancel),
                    onClick = {
                        navController.popBackStack()
                    }
                ),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.apply),
                    onClick = {
                        if (vm.savePrimaryColor())
                            navController.popBackStack()
                    }
                )
            )

            ListItem(
                mainText = stringResource(R.string.primary_color),
                rightText = primaryColorsSheetItems().find {
                    it.obj == vm.primaryColor.value
                }?.name ?: "",
                onClick = { isSheetOpen.value = true }
            )
        }

        if (vm.error.value) Text(
            text = stringResource(R.string.update_saving_error),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondaryContainer
        )

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        if (isSheetOpen.value) ModalSheet(
            sheetState = sheetState,
            sheetItems = primaryColorsSheetItems(),
            closeSheet = { obj ->
                vm.resetError()
                if (obj is PrimaryColor)
                    vm.setPrimaryColor(obj)
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion { isSheetOpen.value = false }
            }
        ).also { coroutineScope.launch { sheetState.show() } }
    }
}
