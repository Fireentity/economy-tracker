package it.unipd.dei.music_application.ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import java.time.LocalDateTime

@Composable
fun MovementDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var amountFieldState by remember { mutableStateOf(TextFieldValue()) }
    var date by remember { mutableStateOf(TextFieldValue()) }

    var selectedTime by remember { mutableStateOf("") }

    val context = LocalContext.current
    //val calendar = Calendar.getInstance()
    var dateTime = LocalDateTime.now()
    val hour = dateTime.hour
    val minute = dateTime.minute

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        hour, minute, true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Text Inverter") },
        text = {
            Column {
                TextField(
                    value = amountFieldState,
                    onValueChange = { amountFieldState = it },
                    label = { Text("Ammontare") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                );
                TextField(
                    value = selectedTime,
                    onValueChange = { },
                    label = { Text(text = "seleziona data") },
                    modifier = Modifier.clickable { timePickerDialog.show() },
                    readOnly = true
                );
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(amountFieldState.text) }) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}