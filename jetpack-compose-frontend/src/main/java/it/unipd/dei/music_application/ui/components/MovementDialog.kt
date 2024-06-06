package it.unipd.dei.music_application.ui.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import it.unipd.dei.music_application.TimeUtils.zonedDateTimeNow
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.ui.CategorySaver
import it.unipd.dei.music_application.ui.CheckUpsertResult
import it.unipd.dei.music_application.view.CategoryViewModel
import java.util.Calendar
import java.util.UUID

private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            onDateSelected("$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear")
        },
        year, month, day
    )

    datePickerDialog.show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Category, String) -> Unit,
    categoryViewModel: CategoryViewModel
) {
    var selectedAmount by remember { mutableStateOf(TextFieldValue()) }

    categoryViewModel.getAllCategories()
    var expandedDropdownMenu by remember { mutableStateOf(false) }
    var selectedCategory = rememberSaveable(saver = CategorySaver.saver) { Category(
        UUID.randomUUID(),
        "",
        System.currentTimeMillis(),
        System.currentTimeMillis()
    ) }
    val categoryList = categoryViewModel.allCategories.observeAsState(initial = listOf()).value

    val context = LocalContext.current

    //val dateTime = zonedDateTimeNow()
    var selectedDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crea Movimento:") },
        text = {
            Column {

                TextField(
                    value = selectedAmount,
                    onValueChange = { selectedAmount = it },
                    label = { Text("Ammontare") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDropdownMenu,
                    onExpandedChange = { expandedDropdownMenu = it }
                ) {
                    TextField(
                        value = selectedCategory.toString(),
                        onValueChange = {  },
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

                Spacer(
                    modifier = Modifier.height(8.dp).fillMaxWidth()
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .align(Alignment.CenterHorizontally)
                ) {
                    TextButton(
                        onClick = {
                            showDatePicker(context) { date ->
                                selectedDate = date
                            }
                        },
                        content = {
                            Text(
                                text = if (selectedDate == "") "Seleziona la data" else "data: $selectedDate",
                                color = MaterialTheme.colorScheme.onSurface,
                                //fontSize = 16.sp
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    )
                }

            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(selectedAmount.text, selectedCategory, selectedDate)
            }
            ) {
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