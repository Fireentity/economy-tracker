package it.unipd.dei.music_application.ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.ui.CategorySaver
import it.unipd.dei.music_application.utils.Constants.ALL_CATEGORIES_IDENTIFIER
import it.unipd.dei.music_application.view.CategoryViewModel
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    categoryViewModel: CategoryViewModel
) {
    var amountFieldState by remember { mutableStateOf(TextFieldValue()) }

    categoryViewModel.getAllCategories()
    var expandedDropdownMenu by remember { mutableStateOf(false) }
    var selectedCategory = rememberSaveable(saver = CategorySaver.saver) { Category(
        UUID.randomUUID(),
        "",
        System.currentTimeMillis(),
        System.currentTimeMillis()
    ) }
    val categoryList = categoryViewModel.allCategories.observeAsState(initial = listOf()).value

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
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDropdownMenu,
                    onExpandedChange = { expandedDropdownMenu = it }
                ) {
                    TextField(
                        value = selectedCategory.identifier,
                        onValueChange = {},
                        label = { Text(text = "Categoria:") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdownMenu)
                        },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDropdownMenu,
                        onDismissRequest = { expandedDropdownMenu = false }
                    ) {
                        for (category in categoryList) {
                            DropdownMenuItem(
                                text = { Text(text = category.toString()) },
                                onClick = {
                                    selectedCategory = category
                                    expandedDropdownMenu = false
                                }
                            )
                        }

                    }
                }

                TextField(
                    value = selectedTime,
                    onValueChange = { },
                    label = { Text(text = "seleziona data") },
                    modifier = Modifier.clickable { timePickerDialog.show() },
                    readOnly = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(amountFieldState.text) }) {
                Text("Crea")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Elimina")
            }
        }
    )
}