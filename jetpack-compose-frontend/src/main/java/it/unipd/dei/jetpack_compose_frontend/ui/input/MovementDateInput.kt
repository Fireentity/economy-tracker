package it.unipd.dei.jetpack_compose_frontend.ui.input

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import it.unipd.dei.common_backend.utils.DateHelper

@Composable
fun MovementDateInput(
    context: Context,
    date: String,
    onDateChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        readOnly = true,
        value = date,
        onValueChange = onDateChange,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showPicker = true },
        label = { Text(text = "Date") },
        trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Select date"
                )
            }
        }
    )

    if (showPicker) {
        LaunchedEffect(Unit) {
            DateHelper.selectDateTime(context, { showPicker = false }) {
                onDateChange(it)
            }
        }
    }
}

