package com.example.finances.core.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.finances.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    isCalendarOpen: Boolean,
    initialDate: LocalDate,
    closeCalendar: (LocalDate?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return !utcTimeMillis.toLocalDate().isAfter(LocalDate.now())
            }
        }
    )

    val datePickerColors = DatePickerDefaults.colors(
        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
        selectedDayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        todayDateBorderColor = MaterialTheme.colorScheme.primary,
        todayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        headlineContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationContentColor = MaterialTheme.colorScheme.onPrimary,
        dividerColor = MaterialTheme.colorScheme.primaryContainer,
        subheadContentColor = MaterialTheme.colorScheme.onPrimary,
        dateTextFieldColors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            disabledLabelColor = Color.Transparent,
            focusedPlaceholderColor = Color.Transparent,
            unfocusedPlaceholderColor = Color.Transparent,
            disabledPlaceholderColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,

            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.onSurface
        )
    )

    if (isCalendarOpen) DatePickerDialog(
        onDismissRequest = { closeCalendar(null) },
        confirmButton = {
            TextButton(
                onClick = {
                    closeCalendar(datePickerState.selectedDateMillis?.toLocalDate())
                }
            ) {
                Text(
                    text = stringResource(R.string.date_picker_ok),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { closeCalendar(null) }) {
                Text(
                    text = stringResource(R.string.date_picker_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = datePickerColors
    ) {
        DatePicker(
            state = datePickerState,
            colors = datePickerColors
        )
    }
}

private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}
