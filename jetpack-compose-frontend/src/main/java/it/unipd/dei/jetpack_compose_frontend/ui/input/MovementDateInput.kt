package it.unipd.dei.jetpack_compose_frontend.ui.input

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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

    var formattedDate: String by remember {
        mutableStateOf(initialDate?.let {
            DateHelper.convertFromMillisecondsToDateTime(it)
        } ?: "")
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
                    contentDescription = stringResource(id = R.string.insert_movement_date)
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

            if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                datePickerState.displayMode = DisplayMode.Input
                DatePicker(state = datePickerState, showModeToggle = false)
            } else {
                DatePicker(state = datePickerState)
            }
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
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT){
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            text = stringResource(id = R.string.select_time),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        TimePicker(
                            state = timePickerState,
                            layoutType = TimePickerLayoutType.Horizontal
                        )
                    } else {
                        TimePicker(state = timePickerState)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { showTimePicker = false }
                        ) { Text(stringResource(id = R.string.cancel)) }
                        TextButton(
                            onClick = {
                                showTimePicker = false
                                datePickerState.selectedDateMillis?.let {
                                    val date =
                                        it + timePickerState.hour * MILLISECONDS_PER_HOUR +
                                                timePickerState.minute * MILLISECONDS_PER_MINUTE
                                    onDateChange(
                                        date
                                    )

                                    formattedDate =
                                        DateHelper.convertFromMillisecondsToDateTime(date)
                                }
                            }
                        ) { Text(stringResource(id = R.string.confirm)) }
                    }
                }
            }
        }
    }
}

