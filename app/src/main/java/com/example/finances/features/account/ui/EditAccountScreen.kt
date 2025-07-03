package com.example.finances.features.account.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.R
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.TextInput
import com.example.finances.features.account.domain.currencyModalItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    navController: NavController,
    vm: EditAccountViewModel = viewModel(factory = EditAccountViewModel.Factory())
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val uploadError = Toast.makeText(
        LocalContext.current,
        R.string.error_sending_data,
        Toast.LENGTH_SHORT
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = stringResource(R.string.my_account),
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.cancel),
                    onClick = { navController.popBackStack() }
                ),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.apply),
                    onClick = {
                        val success = vm.saveChanges()
                        coroutineScope.launch {
                            if (success.await())
                                navController.popBackStack()
                            else
                                uploadError.show()
                        }
                    }
                )
            )

            val balanceFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.balance),
                height = ListItemHeight.LOW,
                emoji = stringResource(R.string.money_bag),
                onClick = { balanceFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.state.value.balance,
                        updateText = { vm.updateBalance(it) },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .focusRequester(balanceFocus)
                            .focusable()
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )

            val accountNameFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.account_name),
                height = ListItemHeight.LOW,
                onClick = { accountNameFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.state.value.accountName,
                        updateText = { vm.updateAccountName(it) },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .focusRequester(accountNameFocus)
                            .focusable()
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )

            ListItem(
                mainText = stringResource(R.string.currency),
                height = ListItemHeight.LOW,
                rightText = vm.state.value.currency,
                onClick = { coroutineScope.launch { sheetState.show() } },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )
        }

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }

    ModalBottomSheet(
        onDismissRequest = { coroutineScope.launch { sheetState.hide() } },
        sheetState = sheetState,
        shape = RoundedCornerShape(36.dp, 36.dp, 0.dp, 0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                width = 32.dp,
                height = 4.dp,
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.outline
            )
        }
    ) {
        currencyModalItems().forEach { currency ->
            ModalItem(
                text = currency.name,
                icon = currency.icon,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable {
                    vm.updateCurrency(currency.obj)
                    coroutineScope.launch { sheetState.hide() }
                }
            )
        }
        ModalItem(
            text = stringResource(R.string.cancel),
            icon = painterResource(R.drawable.modal_cancel),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { coroutineScope.launch { sheetState.hide() } }
        )
    }
}

@Composable
fun ModalItem(
    text: String,
    icon: Painter,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
