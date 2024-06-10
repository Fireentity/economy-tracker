package it.unipd.dei.jetpack_compose_frontend.ui.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.utils.DateHelper
import it.unipd.dei.jetpack_compose_frontend.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDateInput(
    onDateChange: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showPicker by remember { mutableStateOf(false) }
    val date by remember {
        derivedStateOf {
            val computedDate = datePickerState.selectedDateMillis?.let {
                DateHelper.convertFromMillisecondsToDateTime(
                    it
                )
            } ?: ""

            onDateChange(computedDate)
            computedDate
        }
    }

    OutlinedTextField(
        readOnly = true,
        value = date,
        onValueChange = onDateChange,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showPicker = true },
        label = { Text(text = stringResource(R.string.date)) },
        trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = stringResource(R.string.choose_movement_date)
                )
            }
        }
    )

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

