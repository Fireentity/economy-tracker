package it.unipd.dei.jetpack_compose_frontend.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import it.unipd.dei.common_backend.utils.DateHelper
import it.unipd.dei.jetpack_compose_frontend.R

private const val MILLISECONDS_PER_HOUR = 60 * 60 * 1000;
private const val MILLISECONDS_PER_MINUTE = 60 * 1000;

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDateInput(
    initialDate: Long?,
    onDateChange: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val formattedDate: String by remember {
        derivedStateOf {
            initialDate?.let {
                DateHelper.convertFromMillisecondsToDateTime(it)
            } ?: ""
        }
    }

    OutlinedTextField(
        readOnly = true,
        value = formattedDate,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },
        label = {
            Text(stringResource(R.string.insert_movement_date))
        },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Movement date"
                )
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    if (showTimePicker) {
        Dialog(
            onDismissRequest = { showTimePicker = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = "Select time",
                        style = MaterialTheme.typography.labelMedium
                    )
                    TimePicker(state = timePickerState)
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = { showTimePicker = false }
                        ) { Text("Cancel") }
                        TextButton(
                            onClick = {
                                showTimePicker = false
                                datePickerState.selectedDateMillis?.let {
                                    onDateChange(
                                        it + timePickerState.hour * MILLISECONDS_PER_HOUR +
                                                timePickerState.minute * MILLISECONDS_PER_MINUTE
                                    )
                                }
                            }
                        ) { Text(stringResource(id = R.string.confirm)) }
                    }
                }
            }
        }
    }
}

