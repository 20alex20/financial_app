package com.example.finances.features.transactions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.R
import com.example.finances.core.ui.components.Calendar
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.ui.components.ModalSheet
import com.example.finances.core.ui.components.TextInput
import com.example.finances.core.ui.components.models.HeaderButton
import com.example.finances.core.ui.components.models.SheetItem
import com.example.finances.features.transactions.navigation.ScreenType
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUpdateScreen(screenType: ScreenType, transactionId: Int?, navController: NavController) {
    val vm: CreateUpdateViewModel = viewModel(
        factory = LocalViewModelFactory.current,
        extras = remember {
            MutableCreationExtras().apply {
                set(ViewModelParams.Screen, screenType)
                set(ViewModelParams.TransactionId, transactionId)
            }
        }
    )

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val isSheetOpen = remember { mutableStateOf(false) }
    var isCalendarOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        ) {
            Header(
                title = when (screenType) {
                    ScreenType.Expenses -> stringResource(R.string.my_expenses)
                    ScreenType.Income -> stringResource(R.string.my_income)
                },
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.cancel),
                    onClick = {
                        focusManager.clearFocus()
                        navController.popBackStack()
                    }
                ),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.apply),
                    onClick = {
                        focusManager.clearFocus()
                        val success = vm.saveChanges()
                        coroutineScope.launch {
                            if (success.await())
                                navController.popBackStack()
                        }
                    }
                )
            )

            ListItem(
                mainText = stringResource(R.string.category),
                rightText = vm.state.value.categoryName,
                onClick = {
                    focusManager.clearFocus()
                    isSheetOpen.value = true
                },
                modifier = Modifier
            )

            val amountFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.amount),
                paddingValues = PaddingValues(16.dp, 0.dp, 0.dp, 0.dp),
                onClick = { amountFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.state.value.amount,
                        updateText = { vm.updateAmount(it) },
                        textAlign = TextAlign.End,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .focusRequester(amountFocus)
                    )
                }
            )

            ListItem(
                mainText = stringResource(R.string.date),
                rightText = vm.state.value.date,
                onClick = { isCalendarOpen = true }
            )

            val timeFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.time),
                paddingValues = PaddingValues(16.dp, 0.dp, 0.dp, 0.dp),
                onClick = { timeFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.state.value.time,
                        updateText = { vm.updateTime(it) },
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .focusRequester(timeFocus)
                    )
                }
            )

            val commentFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.comment),
                paddingValues = PaddingValues(16.dp, 0.dp, 0.dp, 0.dp),
                onClick = { commentFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.state.value.comment,
                        updateText = { vm.updateComment(it) },
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .focusRequester(commentFocus)
                    )
                }
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
            ) {
                if (vm.loading.value)
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                if (vm.sendingError.value) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.error_sending_data),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Button(
                            onClick = {
                                val success = vm.saveChanges()
                                coroutineScope.launch {
                                    if (success.await())
                                        navController.popBackStack()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.error_retry_sending),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }
                }
            }
        }

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        if (isSheetOpen.value) ModalSheet(
            sheetState = sheetState,
            sheetItems = vm.categories.value.map { SheetItem(it.id, it.name, null) },
            closeSheet = { obj ->
                if (obj is Int) vm.updateCategory(obj)
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion { isSheetOpen.value = false }
            }
        ).also { coroutineScope.launch { sheetState.show() } }

        Calendar(
            isCalendarOpen = isCalendarOpen,
            initialDate = LocalDate.now(),
            closeCalendar = { newDate ->
                isCalendarOpen = false
                if (newDate != null)
                    vm.updateDate(newDate)
            }
        )
    }
}
